package com.guseok.guseokbackend.dto.missingperson;

import com.guseok.guseokbackend.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "실종자 등록 요청")
public record MissingPersonCreateRequest(

    @Schema(description = "이름", example = "이서연")
    @NotBlank(message = "이름은 필수입니다.")
    String name,

    @Schema(description = "나이", example = "17")
    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    Integer age,

    @Schema(description = "성별 (MALE / FEMALE)", example = "FEMALE")
    @NotNull(message = "성별은 필수입니다.")
    Gender gender,

    @Schema(description = "키 (cm)", example = "160")
    @NotNull(message = "키는 필수입니다.")
    @Min(value = 0, message = "키는 0 이상이어야 합니다.")
    Integer height,

    @Schema(description = "몸무게 (kg)", example = "47")
    @NotNull(message = "몸무게는 필수입니다.")
    @Min(value = 0, message = "몸무게는 0 이상이어야 합니다.")
    Integer weight,

    @Schema(description = "인상착의 (선택)", example = "긴 흑발, 교복 착용, 마른 체형")
    String appearanceDescription,

    @Schema(description = "사진 OCI 오브젝트 키 (image-upload-url로 발급받은 objectKey, 선택)", example = "missing-person-images/uuid.jpg")
    String photoObjectKey
) {}
