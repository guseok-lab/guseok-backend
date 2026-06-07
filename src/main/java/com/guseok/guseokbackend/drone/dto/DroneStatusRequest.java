package com.guseok.guseokbackend.drone.dto;

import com.guseok.guseokbackend.entity.DroneStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneStatusRequest {

    @NotBlank
    private String droneId;

    private Long searchId;

    @NotNull
    private DroneStatus status;
}