package com.guseok.guseokbackend.dto.search;

import com.guseok.guseokbackend.entity.SearchMode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "탐색 생성 요청")
public record SearchCreateRequest(

    @Schema(description = "성별", example = "남성")
    @NotBlank(message = "성별은 필수입니다.")
    String gender,

    @Schema(description = "키 (cm)", example = "175")
    @NotNull(message = "키는 필수입니다.")
    @Positive(message = "키는 양수여야 합니다.")
    Integer height,

    @Schema(description = "몸무게 (kg)", example = "70")
    @NotNull(message = "몸무게는 필수입니다.")
    @Positive(message = "몸무게는 양수여야 합니다.")
    Integer weight,

    @Schema(description = "인상착의", example = "파란 재킷, 청바지 착용")
    String appearance,

    @Schema(description = "탐색 모드 (VIDEO: 영상 분석 / DRONE: 드론 연결)", example = "VIDEO")
    @NotNull(message = "탐색 모드는 필수입니다.")
    SearchMode searchMode,

    @Schema(description = "기준 사진 OCI 오브젝트 키 (image-upload-url로 발급받은 objectKey)", example = "images/uuid.jpg")
    @NotBlank(message = "기준 사진은 필수입니다.")
    String targetImageObjectKey
) {}
