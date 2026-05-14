package com.guseok.guseokbackend.drone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.drone.dto.DroneStatusRequest;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.dto.StreamUrlRequest;
import com.guseok.guseokbackend.drone.entity.Drone;
import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;
import com.guseok.guseokbackend.drone.repository.DroneRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneCallbackService {

    private final DroneRepository droneRepository;

    // 맥북 stream_server.py 시작 시 스트림 URL 등록
    @Transactional
    public DroneStatusResponse registerStream(StreamUrlRequest request) {
        Drone drone = droneRepository.findByStatus(DroneConnectionStatus.AVAILABLE)
            .orElseGet(Drone::create);

        drone.connect(request.getStreamUrl());
        droneRepository.save(drone);

        log.info("[드론] 스트림 등록 완료 - URL: {}", request.getStreamUrl());
        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    // 맥북 stream_server.py 상태 변경 시 호출
    @Transactional
    public DroneStatusResponse updateStatus(DroneStatusRequest request) {
        Drone drone = droneRepository.findById(request.getDroneId())
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        if (request.getStatus() == DroneConnectionStatus.DISCONNECTED) {
            drone.disconnect();
        }

        droneRepository.save(drone);

        log.info("[드론] 상태 업데이트 - droneId: {}, status: {}", drone.getId(), drone.getStatus());
        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }
}