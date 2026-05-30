package com.guseok.guseokbackend.service;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.dto.file.AiCallbackRequest;
import com.guseok.guseokbackend.dto.file.AiResultUploadUrlResponse;
import com.guseok.guseokbackend.dto.file.ImageUploadUrlResponse;
import com.guseok.guseokbackend.dto.file.UploadUrlResponse;
import com.guseok.guseokbackend.dto.search.SearchCreateRequest;
import com.guseok.guseokbackend.dto.search.SearchCreateResponse;
import com.guseok.guseokbackend.dto.search.SearchDetailResponse;
import com.guseok.guseokbackend.dto.search.SearchResultResponse;
import com.guseok.guseokbackend.dto.search.VideoUploadResponse;
import com.guseok.guseokbackend.entity.ResultType;
import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchResult;
import com.guseok.guseokbackend.entity.SearchResultStatus;
import com.guseok.guseokbackend.entity.SearchStatus;
import com.guseok.guseokbackend.entity.SearchVideo;
import com.guseok.guseokbackend.repository.SearchRepository;
import com.guseok.guseokbackend.repository.SearchResultRepository;
import com.guseok.guseokbackend.repository.SearchVideoRepository;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final SearchVideoRepository searchVideoRepository;
    private final SearchResultRepository searchResultRepository;
    private final OciStorageService ociStorageService;
    private final AiRequestService aiRequestService;

    // ─── 이미지 업로드 URL 발급 ───────────────────────────────────────────────

    public ImageUploadUrlResponse generateImageUploadUrl(String originalFilename) {
        String objectKey = "images/" + UUID.randomUUID() + "." + extractExtension(originalFilename);
        String uploadUrl = ociStorageService.generatePresignedPutUrl(objectKey);
        return new ImageUploadUrlResponse(objectKey, uploadUrl);
    }

    // ─── AI 결과 이미지 업로드 URL 발급 ──────────────────────────────────────

    public AiResultUploadUrlResponse generateResultUploadUrl(Long searchId, String originalFilename) {
        getSearch(searchId); // searchId 유효성 확인
        String objectKey = "results/" + searchId + "/" + UUID.randomUUID() + "." + extractExtension(originalFilename);
        String uploadUrl = ociStorageService.generatePresignedPutUrl(objectKey);
        String resultImageUrl = ociStorageService.buildObjectUrl(objectKey);
        return new AiResultUploadUrlResponse(objectKey, uploadUrl, resultImageUrl);
    }

    // ─── 탐색 생성 (JSON, targetImageObjectKey 포함) ──────────────────────────

    @Transactional
    public SearchCreateResponse createSearch(SearchCreateRequest request) {
        String targetImageUrl = ociStorageService.buildObjectUrl(request.targetImageObjectKey());

        Search search = Search.builder()
            .gender(request.gender())
            .height(request.height())
            .weight(request.weight())
            .appearance(request.appearance())
            .targetImageUrl(targetImageUrl)
            .searchMode(request.searchMode())
            .status(SearchStatus.REQUESTED)
            .build();

        return SearchCreateResponse.from(searchRepository.save(search));
    }

    // ─── 탐색 상세 조회 ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public SearchDetailResponse getSearchDetail(Long searchId) {
        Search search = getSearch(searchId);
        String presignedTargetImageUrl = ociStorageService.generatePresignedGetUrl(
            ociStorageService.extractObjectKey(search.getTargetImageUrl()));
        List<String> videoUrls = searchVideoRepository.findBySearch(search).stream()
            .map(v -> ociStorageService.generatePresignedGetUrl(v.getObjectKey()))
            .toList();
        return SearchDetailResponse.from(search, presignedTargetImageUrl, videoUrls);
    }

    // ─── 영상 업로드 URL 발급 ─────────────────────────────────────────────────

    @Transactional
    public UploadUrlResponse generateVideoUploadUrl(Long searchId, String originalFilename) {
        Search search = getSearch(searchId);

        String objectKey = "videos/" + searchId + "/" + UUID.randomUUID() + "." + extractExtension(originalFilename);
        SearchVideo video = SearchVideo.builder()
            .search(search)
            .objectKey(objectKey)
            .build();
        SearchVideo saved = searchVideoRepository.save(video);

        search.updateStatus(SearchStatus.UPLOAD_REQUESTED);

        String uploadUrl = ociStorageService.generatePresignedPutUrl(objectKey);
        return new UploadUrlResponse(saved.getId(), searchId, objectKey, uploadUrl);
    }

    // ─── 영상 업로드 완료 보고 + AI 분석 요청 ────────────────────────────────

    @Transactional
    public VideoUploadResponse completeVideoUpload(Long searchId, Long videoId) {
        Search search = getSearch(searchId);
        SearchVideo video = searchVideoRepository.findById(videoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_FOUND));

        if (!video.getSearch().getId().equals(searchId)) {
            throw new BusinessException(ErrorCode.SEARCH_ACCESS_DENIED);
        }

        if (!ociStorageService.doesObjectExist(video.getObjectKey())) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }

        video.updateVideoUrl(ociStorageService.buildObjectUrl(video.getObjectKey()));
        search.updateStatus(SearchStatus.PENDING);

        try {
            String presignedVideoUrl = ociStorageService.generatePresignedGetUrl(video.getObjectKey());
            String presignedTargetImageUrl = ociStorageService.generatePresignedGetUrl(
                ociStorageService.extractObjectKey(search.getTargetImageUrl()));
            aiRequestService.requestAnalysis(search.getId(), presignedVideoUrl, presignedTargetImageUrl);
        } catch (BusinessException e) {
            log.warn("AI 분석 요청 실패 - searchId: {}, 상태는 PENDING 유지", search.getId());
        }

        return VideoUploadResponse.from(video);
    }

    // ─── AI 콜백 수신 ─────────────────────────────────────────────────────────

    @Transactional
    public void handleAiCallback(AiCallbackRequest request) {
        Search search = getSearch(request.searchId());

        SearchStatus newStatus = "FAILED".equals(request.status())
            ? SearchStatus.FAILED
            : SearchStatus.COMPLETED;
        search.updateStatus(newStatus);

        if (request.results() != null && !request.results().isEmpty()) {
            List<SearchResult> results = request.results().stream()
                .map(item -> SearchResult.builder()
                    .search(search)
                    .resultType(ResultType.valueOf(item.resultType()))
                    .status(SearchResultStatus.valueOf(item.status()))
                    .accuracy(item.accuracy())
                    .matchedImageUrl(item.matchedImageUrl())
                    .matchedTimeSeconds(item.matchedTimeSeconds())
                    .build())
                .toList();
            searchResultRepository.saveAll(results);
        }
    }

    // ─── 분석 결과 조회 ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<SearchResultResponse> getSearchResults(Long searchId) {
        Search search = getSearch(searchId);
        return searchResultRepository.findBySearch(search)
            .stream()
            .map(result -> {
                String presignedImageUrl = result.getMatchedImageUrl() != null
                    ? ociStorageService.generatePresignedGetUrl(
                        ociStorageService.extractObjectKey(result.getMatchedImageUrl()))
                    : null;
                return SearchResultResponse.from(result, presignedImageUrl);
            })
            .toList();
    }

    // ─── 공통 ─────────────────────────────────────────────────────────────────

    Search getSearch(Long searchId) {
        return searchRepository.findById(searchId)
            .orElseThrow(() -> new BusinessException(ErrorCode.SEARCH_NOT_FOUND));
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "bin";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}