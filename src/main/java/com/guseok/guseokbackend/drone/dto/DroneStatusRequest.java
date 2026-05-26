package com.guseok.guseokbackend.drone.dto;

import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DroneStatusRequest {

    @NotBlank
    private String droneId;     // drone-xxxxxxxx (Long → String)

    private Long searchId;      // nullable

    @NotNull
    private DroneConnectionStatus status;
}