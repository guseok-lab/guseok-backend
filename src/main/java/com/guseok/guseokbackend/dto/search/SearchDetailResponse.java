package com.guseok.guseokbackend.dto.search;

import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchMode;
import com.guseok.guseokbackend.entity.SearchStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "탐색 상세 조회 응답")
public record SearchDetailResponse(

    @Schema(description = "탐색 ID", example = "1")
    Long searchId,

    @Schema(description = "성별", example = "남성")
    String gender,

    @Schema(description = "키 (cm)", example = "175")
    Integer height,

    @Schema(description = "몸무게 (kg)", example = "70")
    Integer weight,

    @Schema(description = "인상착의", example = "파란 재킷, 청바지 착용")
    String appearance,

    @Schema(description = "기준 사진 경로")
    String targetImageUrl,

    @Schema(description = "탐색 모드", example = "VIDEO")
    SearchMode searchMode,

    @Schema(description = "탐색 상태", example = "REQUESTED")
    SearchStatus status
) {
    public static SearchDetailResponse from(Search search) {
        return new SearchDetailResponse(
            search.getId(),
            search.getGender(),
            search.getHeight(),
            search.getWeight(),
            search.getAppearance(),
            search.getTargetImageUrl(),
            search.getSearchMode(),
            search.getStatus()
        );
    }
}