package com.guseok.guseokbackend.controller;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.dto.search.SearchCreateRequest;
import com.guseok.guseokbackend.dto.search.SearchCreateResponse;
import com.guseok.guseokbackend.dto.search.SearchDetailResponse;
import com.guseok.guseokbackend.dto.search.VideoUploadResponse;
import com.guseok.guseokbackend.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Search", description = "실종자 탐색 API")
@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(
        summary = "탐색 생성",
        description = "실종자 탐색 요청을 생성합니다. 비회원도 사용 가능합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE),
                schema = @Schema(implementation = SearchController.CreateSearchForm.class)
            )
        )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<SearchCreateResponse>> createSearch(
        @RequestPart("request") @Valid SearchCreateRequest request,
        @RequestPart("targetImage") MultipartFile targetImage
    ) {
        return ResponseEntity.ok(ApiResponse.success(searchService.createSearch(request, targetImage)));
    }

    @Operation(summary = "탐색 상세 조회", description = "탐색 ID로 상세 정보를 조회합니다. 비회원도 사용 가능합니다.")
    @GetMapping("/{searchId}")
    public ResponseEntity<ApiResponse<SearchDetailResponse>> getSearch(
        @Parameter(description = "탐색 ID", example = "1") @PathVariable Long searchId
    ) {
        return ResponseEntity.ok(ApiResponse.success(searchService.getSearchDetail(searchId)));
    }

    @Operation(
        summary = "영상 첨부",
        description = "탐색에 영상을 첨부합니다. 첨부 후 탐색 상태가 IN_PROGRESS로 변경됩니다. 비회원도 사용 가능합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                schema = @Schema(implementation = SearchController.UploadVideoForm.class)
            )
        )
    )
    @PostMapping(value = "/{searchId}/videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<VideoUploadResponse>> uploadVideo(
        @Parameter(description = "탐색 ID", example = "1") @PathVariable Long searchId,
        @RequestPart("video") MultipartFile video
    ) {
        return ResponseEntity.ok(ApiResponse.success(searchService.uploadVideo(searchId, video)));
    }

    @Schema(description = "탐색 생성 multipart 폼")
    record CreateSearchForm(
        @Schema(description = "탐색 요청 JSON (application/json)", implementation = SearchCreateRequest.class)
        SearchCreateRequest request,
        @Schema(description = "기준 사진 파일", type = "string", format = "binary")
        MultipartFile targetImage
    ) {}

    @Schema(description = "영상 첨부 multipart 폼")
    record UploadVideoForm(
        @Schema(description = "영상 파일", type = "string", format = "binary")
        MultipartFile video
    ) {}
}
