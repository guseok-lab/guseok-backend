package com.guseok.guseokbackend.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 업로드 URL 발급 응답")
public record ImageUploadUrlResponse(

    @Schema(description = "OCI 오브젝트 키", example = "images/uuid.jpg")
    String objectKey,

    @Schema(description = "업로드 전용 Presigned PUT URL (10분 유효)")
    String uploadUrl
) {}