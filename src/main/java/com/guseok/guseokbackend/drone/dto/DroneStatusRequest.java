package com.guseok.guseokbackend.drone.dto;

import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DroneStatusRequest {

    @NotNull
    private Long droneId;

    @NotNull
    private DroneConnectionStatus status;
}