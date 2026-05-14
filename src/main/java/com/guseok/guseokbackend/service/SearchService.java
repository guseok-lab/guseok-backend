package com.guseok.guseokbackend.service;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.dto.search.SearchCreateRequest;
import com.guseok.guseokbackend.dto.search.SearchCreateResponse;
import com.guseok.guseokbackend.dto.search.SearchDetailResponse;
import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchMode;
import com.guseok.guseokbackend.entity.SearchStatus;
import com.guseok.guseokbackend.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public SearchCreateResponse createSearch(SearchCreateRequest request, MultipartFile targetImage) {
        String imagePath = fileStorageService.storeImage(targetImage);

        Search search = Search.builder()
            .gender(request.gender())
            .height(request.height())
            .weight(request.weight())
            .appearance(request.appearance())
            .targetImageUrl(imagePath)
            .searchMode(request.searchMode())
            .status(SearchStatus.REQUESTED)
            .build();

        return SearchCreateResponse.from(searchRepository.save(search));
    }

    @Transactional(readOnly = true)
    public SearchDetailResponse getSearchDetail(Long searchId) {
        return SearchDetailResponse.from(getSearch(searchId));
    }

    Search getSearch(Long searchId) {
        return searchRepository.findById(searchId)
            .orElseThrow(() -> new BusinessException(ErrorCode.SEARCH_NOT_FOUND));
    }
}
