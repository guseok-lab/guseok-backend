package com.guseok.guseokbackend.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "업로드 URL 발급 응답")
public record UploadUrlResponse(

    @Schema(description = "영상 ID", example = "1")
    Long videoId,

    @Schema(description = "탐색 ID", example = "1")
    Long searchId,

    @Schema(description = "OCI 오브젝트 키", example = "videos/1/uuid.mp4")
    String objectKey,

    @Schema(description = "업로드 전용 Presigned PUT URL (10분 유효)")
    String uploadUrl
) {}