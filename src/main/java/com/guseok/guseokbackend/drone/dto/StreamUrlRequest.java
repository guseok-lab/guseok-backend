package com.guseok.guseokbackend.drone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StreamUrlRequest {
    @NotBlank
    private String droneId;     // drone-xxxxxxxx (String)
    private Long searchId;      // nullable (searchId 없이 테스트 접속 가능)

    @NotBlank
    private String streamUrl;

    @NotNull
    private Boolean connected;
}