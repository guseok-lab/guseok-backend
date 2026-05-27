package com.guseok.guseokbackend.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "AI 분석 콜백 요청")
public record AiCallbackRequest(

    @Schema(description = "탐색 ID", example = "1")
    @NotNull Long searchId,

    @Schema(description = "분석 상태 (COMPLETED / FAILED)", example = "COMPLETED")
    @NotBlank String status,

    @Schema(description = "실패 사유 (status가 FAILED일 때)", example = "분석 중 오류 발생")
    String errorMessage,

    @Schema(description = "분석 결과 목록")
    List<AiResultItem> results
) {
    @Schema(description = "개별 분석 결과")
    public record AiResultItem(

        @Schema(description = "결과 유형 (VIDEO / DRONE)", example = "VIDEO")
        String resultType,

        @Schema(description = "탐지 상태 (FOUND / NOT_FOUND / FAILED)", example = "FOUND")
        String status,

        @Schema(description = "유사도 (0.0~1.0)", example = "0.95")
        Double accuracy,

        @Schema(description = "탐지 이미지 URL")
        String matchedImageUrl,

        @Schema(description = "탐지된 영상 시점 (초)", example = "125")
        Integer matchedTimeSeconds
    ) {}
}