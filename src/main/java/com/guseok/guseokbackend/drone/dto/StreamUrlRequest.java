package com.guseok.guseokbackend.drone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StreamUrlRequest {
    @NotBlank
    private String streamUrl;
}