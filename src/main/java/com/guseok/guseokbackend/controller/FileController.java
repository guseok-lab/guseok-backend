package com.guseok.guseokbackend.controller;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.dto.file.AiCallbackRequest;
import com.guseok.guseokbackend.dto.file.AiResultUploadUrlResponse;
import com.guseok.guseokbackend.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "File", description = "파일 처리 API")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final SearchService searchService;

    @Operation(
        summary = "AI 결과 이미지 업로드 URL 발급",
        description = "AI 서버가 분석 결과 이미지를 OCI에 직접 업로드할 수 있도록 Presigned PUT URL을 발급합니다. " +
                      "업로드 완료 후 응답의 resultImageUrl을 콜백의 matchedImageUrl에 사용하세요."
    )
    @GetMapping("/result-upload-url")
    public ResponseEntity<ApiResponse<AiResultUploadUrlResponse>> getResultUploadUrl(
        @Parameter(description = "탐색 ID", example = "1") @RequestParam Long searchId,
        @Parameter(description = "파일명 (확장자 추출용)", example = "result.jpg") @RequestParam String filename
    ) {
        return ResponseEntity.ok(ApiResponse.success(searchService.generateResultUploadUrl(searchId, filename)));
    }

    @Operation(
        summary = "AI 분석 콜백 수신",
        description = "AI 서버의 분석 완료 콜백을 수신합니다. 탐색 상태가 COMPLETED 또는 FAILED로 변경됩니다."
    )
    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<Void>> handleAiCallback(
        @RequestBody @Valid AiCallbackRequest request
    ) {
        searchService.handleAiCallback(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}