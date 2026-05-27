package com.guseok.guseokbackend.drone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.drone.dto.DroneStatusRequest;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.dto.StreamUrlRequest;
import com.guseok.guseokbackend.drone.repository.DroneRepository;
import com.guseok.guseokbackend.entity.Drone;
import com.guseok.guseokbackend.entity.DroneStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneCallbackService {

    private final DroneRepository droneRepository;

    @Transactional
    public DroneStatusResponse registerStream(StreamUrlRequest request) {
        Drone drone = droneRepository.findByDroneId(request.getDroneId())
            .orElseGet(() -> Drone.builder()
                .droneName(request.getDroneId())
                .status(DroneStatus.AVAILABLE)
                .build());

        if (Boolean.TRUE.equals(request.getConnected())) {
            drone.connectStream(request.getDroneId(), request.getStreamUrl(), request.getSearchId());
        } else {
            drone.disconnectStream();
        }

        droneRepository.save(drone);
        log.info("[드론] 스트림 등록 - droneId: {}, searchId: {}, connected: {}",
            request.getDroneId(), request.getSearchId(), request.getConnected());

        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    @Transactional
    public DroneStatusResponse updateStatus(DroneStatusRequest request) {
        Drone drone = droneRepository.findByDroneId(request.getDroneId())
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        if (request.getStatus() == DroneStatus.DISCONNECTED) {
            drone.disconnectStream();
        }

        droneRepository.save(drone);
        log.info("[드론] 상태 업데이트 - droneId: {}, status: {}",
            request.getDroneId(), drone.getStatus());

        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }
}