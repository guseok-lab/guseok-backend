package com.guseok.guseokbackend.dto.search;

import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "탐색 생성 응답")
public record SearchCreateResponse(

    @Schema(description = "탐색 ID", example = "1")
    Long searchId,

    @Schema(description = "탐색 상태", example = "REQUESTED")
    SearchStatus status
) {
    public static SearchCreateResponse from(Search search) {
        return new SearchCreateResponse(search.getId(), search.getStatus());
    }
}
