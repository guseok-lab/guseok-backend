package com.guseok.guseokbackend.drone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.repository.DroneRepository;
import com.guseok.guseokbackend.entity.Drone;
import com.guseok.guseokbackend.entity.DroneStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;

    @Transactional(readOnly = true)
    public DroneStatusResponse getAvailable() {
        Drone drone = droneRepository.findFirstByStatusOrderByIdDesc(DroneStatus.CONNECTED)
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_AVAILABLE));
        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }
}