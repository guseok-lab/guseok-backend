package com.guseok.guseokbackend.dto.file;

import com.guseok.guseokbackend.entity.SearchStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "업로드 완료 응답")
public record UploadCompleteResponse(

    @Schema(description = "영상 ID", example = "1")
    Long videoId,

    @Schema(description = "탐색 ID", example = "1")
    Long searchId,

    @Schema(description = "탐색 상태", example = "PENDING")
    SearchStatus status
) {}