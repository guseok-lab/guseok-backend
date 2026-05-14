package com.guseok.guseokbackend.dto.search;

import com.guseok.guseokbackend.entity.ResultType;
import com.guseok.guseokbackend.entity.SearchResult;
import com.guseok.guseokbackend.entity.SearchResultStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

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

    @Schema(description = "탐지 이미지 경로")
    String matchedImageUrl,

    @Schema(description = "탐지 영상 경로")
    String matchedVideoUrl,

    @Schema(description = "영상 내 발견 시점 (초)", example = "34")
    Integer matchedTimeSeconds,

    @Schema(description = "위도", example = "37.5665")
    Double latitude,

    @Schema(description = "경도", example = "126.9780")
    Double longitude,

    @Schema(description = "탐지 시각")
    LocalDateTime detectedAt
) {
    public static SearchResultResponse from(SearchResult result) {
        return new SearchResultResponse(
            result.getId(),
            result.getResultType(),
            result.getStatus(),
            result.getAccuracy(),
            result.getMatchedImageUrl(),
            result.getMatchedVideoUrl(),
            result.getMatchedTimeSeconds(),
            result.getLatitude(),
            result.getLongitude(),
            result.getDetectedAt()
        );
    }
}