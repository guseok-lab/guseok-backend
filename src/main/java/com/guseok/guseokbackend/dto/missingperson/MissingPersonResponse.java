package com.guseok.guseokbackend.dto.missingperson;

import com.guseok.guseokbackend.entity.Gender;
import com.guseok.guseokbackend.entity.MissingPerson;
import com.guseok.guseokbackend.entity.MissingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "실종자 정보 응답")
public record MissingPersonResponse(

    @Schema(description = "실종자 ID", example = "1")
    Long missingPersonId,

    @Schema(description = "이름", example = "이서연")
    String name,

    @Schema(description = "나이", example = "17")
    Integer age,

    @Schema(description = "성별", example = "FEMALE")
    Gender gender,

    @Schema(description = "키 (cm)", example = "160")
    Integer height,

    @Schema(description = "몸무게 (kg)", example = "47")
    Integer weight,

    @Schema(description = "인상착의", example = "긴 흑발, 교복 착용, 마른 체형")
    String appearanceDescription,

    @Schema(description = "사진 URL")
    String photoUrl,

    @Schema(description = "상태", example = "SEARCHING")
    MissingStatus status,

    @Schema(description = "등록 일시", example = "2026-05-26T23:10:00")
    LocalDateTime createdAt
) {
    public static MissingPersonResponse from(MissingPerson missingPerson) {
        return new MissingPersonResponse(
            missingPerson.getId(),
            missingPerson.getName(),
            missingPerson.getAge(),
            missingPerson.getGender(),
            missingPerson.getHeight(),
            missingPerson.getWeight(),
            missingPerson.getAppearanceDescription(),
            missingPerson.getPhotoUrl(),
            missingPerson.getStatus(),
            missingPerson.getCreatedAt()
        );
    }
}
