package com.guseok.guseokbackend.controller;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.dto.file.ImageUploadUrlResponse;
import com.guseok.guseokbackend.dto.file.UploadUrlResponse;
import com.guseok.guseokbackend.dto.search.SearchCreateRequest;
import com.guseok.guseokbackend.dto.search.SearchCreateResponse;
import com.guseok.guseokbackend.dto.search.SearchDetailResponse;
import com.guseok.guseokbackend.dto.search.SearchResultResponse;
import com.guseok.guseokbackend.dto.search.VideoUploadResponse;
import com.guseok.guseokbackend.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Search", description = "실종자 탐색 API")
@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
@Validated
public class SearchController {

    private final SearchService searchService;

    // ─── 기준 사진 업로드 URL 발급 ────────────────────────────────────────────

    @Operation(
        summary = "기준 사진 업로드 URL 발급",
        description = "OCI Object Storage에 기준 사진을 직접 업로드하기 위한 10분 유효 Presigned PUT URL을 발급합니다. " +
            "반환된 objectKey를 탐색 생성(POST /) 시 targetImageObjectKey로 사용하세요."
    )
    @GetMapping("/image-upload-url")
    public ResponseEntity<ApiResponse<ImageUploadUrlResponse>> getImageUploadUrl(
        @Parameter(description = "원본 파일명", example = "photo.jpg", required = true)
        @RequestParam @NotBlank String originalFilename
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            searchService.generateImageUploadUrl(originalFilename)
        ));
    }

    // ─── 탐색 생성 ────────────────────────────────────────────────────────────

    @Operation(
        summary = "탐색 생성",
        description = "실종자 탐색 요청을 생성합니다. 비회원도 사용 가능합니다. " +
            "image-upload-url로 기준 사진을 먼저 업로드한 뒤 objectKey를 targetImageObjectKey에 넣어주세요."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<SearchCreateResponse>> createSearch(
        @RequestBody @Valid SearchCreateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(searchService.createSearch(request)));
    }

    // ─── 탐색 상세 조회 ───────────────────────────────────────────────────────

    @Operation(summary = "탐색 상세 조회", description = "탐색 ID로 상세 정보를 조회합니다. 비회원도 사용 가능합니다.")
    @GetMapping("/{searchId}")
    public ResponseEntity<ApiResponse<SearchDetailResponse>> getSearch(
        @Parameter(description = "탐색 ID", example = "1") @PathVariable Long searchId
    ) {
        return ResponseEntity.ok(ApiResponse.success(searchService.getSearchDetail(searchId)));
    }

    // ─── 영상 업로드 URL 발급 ─────────────────────────────────────────────────

    @Operation(
        summary = "영상 업로드 URL 발급",
        description = "OCI Object Storage에 영상을 직접 업로드하기 위한 10분 유효 Presigned PUT URL을 발급합니다. " +
            "반환된 videoId를 업로드 완료 보고(POST /{searchId}/videos/complete) 시 사용하세요."
    )
    @GetMapping("/{searchId}/videos/upload-url")
    public ResponseEntity<ApiResponse<UploadUrlResponse>> getVideoUploadUrl(
        @Parameter(description = "탐색 ID", example = "1") @PathVariable Long searchId,
        @Parameter(description = "원본 파일명", example = "video.mp4", required = true)
        @RequestParam @NotBlank String originalFilename
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            searchService.generateVideoUploadUrl(searchId, originalFilename)
        ));
    }

    // ─── 영상 업로드 완료 보고 ────────────────────────────────────────────────

    @Operation(
        summary = "영상 업로드 완료 보고",
        description = "OCI Object Storage 업로드 완료 후 호출합니다. " +
            "파일 존재 여부를 확인하고 AI 분석을 요청합니다. 탐색 상태가 PENDING으로 변경됩니다."
    )
    @PostMapping("/{searchId}/videos/complete")
    public ResponseEntity<ApiResponse<VideoUploadResponse>> completeVideoUpload(
        @Parameter(description = "탐색 ID", example = "1") @PathVariable Long searchId,
        @Parameter(description = "영상 ID", example = "1", required = true)
        @RequestParam @NotNull Long videoId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            searchService.completeVideoUpload(searchId, videoId)
        ));
    }

    // ─── 분석 결과 조회 ───────────────────────────────────────────────────────

    @Operation(summary = "분석 결과 조회", description = "탐색의 AI 분석 결과 목록을 조회합니다. AI 콜백 수신 전에는 빈 배열을 반환합니다.")
    @GetMapping("/{searchId}/results")
    public ResponseEntity<ApiResponse<List<SearchResultResponse>>> getSearchResults(
        @Parameter(description = "탐색 ID", example = "1") @PathVariable Long searchId
    ) {
        return ResponseEntity.ok(ApiResponse.success(searchService.getSearchResults(searchId)));
    }
}