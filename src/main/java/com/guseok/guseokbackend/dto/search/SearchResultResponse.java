package com.guseok.guseokbackend.dto.search;

import com.guseok.guseokbackend.entity.ResultType;
import com.guseok.guseokbackend.entity.SearchResult;
import com.guseok.guseokbackend.entity.SearchResultStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "분석 결과")
public record SearchResultResponse(

    @Schema(description = "결과 ID", example = "1")
    Long resultId,

    @Schema(description = "결과 유형 (VIDEO / DRONE)", example = "VIDEO")
    ResultType resultType,

    @Schema(description = "결과 상태 (FOUND / NOT_FOUND / FAILED)", example = "FOUND")
    SearchResultStatus status,

    @Schema(description = "유사도 / 정확도 (0.0 ~ 1.0)", example = "0.93")
    Double accuracy,

    @Schema(description = "탐지 이미지 URL")
    String matchedImageUrl,

    @Schema(description = "영상 내 발견 시점 (초)", example = "34")
    Integer matchedTimeSeconds
) {
    public static SearchResultResponse from(SearchResult result) {
        return new SearchResultResponse(
            result.getId(),
            result.getResultType(),
            result.getStatus(),
            result.getAccuracy(),
            result.getMatchedImageUrl(),
            result.getMatchedTimeSeconds()
        );
    }
}