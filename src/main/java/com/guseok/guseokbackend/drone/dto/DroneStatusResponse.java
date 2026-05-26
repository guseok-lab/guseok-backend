package com.guseok.guseokbackend.drone.dto;

import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DroneStatusResponse {
    private final Long droneId;
    private final DroneConnectionStatus status;
    private final String streamUrl;
}