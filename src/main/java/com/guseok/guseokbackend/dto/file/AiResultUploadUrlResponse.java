package com.guseok.guseokbackend.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 결과 이미지 업로드 URL 발급 응답")
public record AiResultUploadUrlResponse(

    @Schema(description = "OCI 오브젝트 키", example = "results/1/uuid.jpg")
    String objectKey,

    @Schema(description = "업로드 전용 Presigned PUT URL (10분 유효)")
    String uploadUrl,

    @Schema(description = "업로드 완료 후 콜백에 사용할 이미지 URL")
    String resultImageUrl
) {}
