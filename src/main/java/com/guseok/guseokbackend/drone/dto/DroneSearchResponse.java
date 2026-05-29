package com.guseok.guseokbackend.drone.dto;

import com.guseok.guseokbackend.drone.enums.SearchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DroneSearchResponse {
    private final Long searchId;
    private final SearchStatus status;
}