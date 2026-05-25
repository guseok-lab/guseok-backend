package com.guseok.guseokbackend.controller;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.dto.file.ImageUploadUrlResponse;
import com.guseok.guseokbackend.dto.missingperson.MissingPersonCreateRequest;
import com.guseok.guseokbackend.dto.missingperson.MissingPersonResponse;
import com.guseok.guseokbackend.service.MissingPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MissingPerson", description = "실종자 정보 API")
@RestController
@RequestMapping("/api/v1/missing-persons")
@RequiredArgsConstructor
@Validated
public class MissingPersonController {

    private final MissingPersonService missingPersonService;

    @Operation(
        summary = "실종자 사진 업로드 URL 발급",
        description = "OCI Object Storage에 사진을 직접 업로드하기 위한 10분 유효 Presigned PUT URL을 발급합니다. " +
            "반환된 objectKey를 실종자 등록(POST /) 시 photoObjectKey로 사용하세요."
    )
    @GetMapping("/image-upload-url")
    public ResponseEntity<ApiResponse<ImageUploadUrlResponse>> getImageUploadUrl(
        @Parameter(description = "원본 파일명", example = "photo.jpg", required = true)
        @RequestParam @NotBlank String originalFilename
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            missingPersonService.generateImageUploadUrl(originalFilename)
        ));
    }

    @Operation(summary = "실종자 정보 등록", description = "로그인한 회원이 실종자 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<MissingPersonResponse>> create(
        @RequestBody @Valid MissingPersonCreateRequest request
    ) {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(missingPersonService.create(memberId, request)));
    }

    @Operation(summary = "내 실종자 목록 조회", description = "현재 로그인한 회원이 등록한 실종자 목록을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<MissingPersonResponse>>> getMyMissingPersons() {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(missingPersonService.getMyMissingPersons(memberId)));
    }

    @Operation(summary = "실종자 정보 삭제", description = "현재 로그인한 회원이 등록한 실종자 정보를 삭제합니다.")
    @DeleteMapping("/{missingPersonId}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @Parameter(description = "실종자 ID", example = "1") @PathVariable Long missingPersonId
    ) {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        missingPersonService.delete(memberId, missingPersonId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
