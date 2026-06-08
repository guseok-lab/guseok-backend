package com.guseok.guseokbackend.drone.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "드론 실시간 탐지 결과 콜백 요청")
public record DroneDetectionRequest(

    @Schema(description = "탐색 ID", example = "1")
    @NotNull Long searchId,

    @Schema(description = "탐지 상태 (FOUND / NOT_FOUND / FAILED)", example = "FOUND")
    @NotBlank String status,

    @Schema(description = "유사도 (0.0~1.0)", example = "0.95")
    Double accuracy,

    @Schema(description = "탐지 이미지 URL")
    String matchedImageUrl
) {}
