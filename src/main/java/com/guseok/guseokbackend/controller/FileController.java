package com.guseok.guseokbackend.controller;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.dto.file.AiCallbackRequest;
import com.guseok.guseokbackend.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "File", description = "파일 처리 API")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final SearchService searchService;

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