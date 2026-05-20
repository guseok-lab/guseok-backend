package com.guseok.guseokbackend.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "영상 업로드 완료 요청")
public record VideoCompleteRequest(

    @Schema(description = "영상 ID", example = "1")
    @NotNull Long videoId
) {}