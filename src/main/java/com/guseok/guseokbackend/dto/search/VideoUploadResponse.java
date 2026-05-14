package com.guseok.guseokbackend.dto.search;

import com.guseok.guseokbackend.entity.SearchStatus;
import com.guseok.guseokbackend.entity.SearchVideo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "영상 첨부 응답")
public record VideoUploadResponse(

    @Schema(description = "탐색 ID", example = "1")
    Long searchId,

    @Schema(description = "영상 ID", example = "1")
    Long videoId,

    @Schema(description = "탐색 상태", example = "IN_PROGRESS")
    SearchStatus status
) {
    public static VideoUploadResponse from(SearchVideo video) {
        return new VideoUploadResponse(
            video.getSearch().getId(),
            video.getId(),
            video.getSearch().getStatus()
        );
    }
}