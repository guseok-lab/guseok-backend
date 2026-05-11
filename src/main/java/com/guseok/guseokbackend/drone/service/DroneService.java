package com.guseok.guseokbackend.drone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.dto.StreamUrlRequest;
import com.guseok.guseokbackend.drone.dto.StreamUrlResponse;
import com.guseok.guseokbackend.drone.entity.Drone;
import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;
import com.guseok.guseokbackend.drone.repository.DroneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;

    // 맥북에서 스트림 URL 등록 + 드론 연결 상태로 변경
    @Transactional
    public DroneStatusResponse registerStreamUrl(StreamUrlRequest request) {
        Drone drone = droneRepository.findByStatus(DroneConnectionStatus.AVAILABLE)
            .orElseGet(Drone::create);

        drone.connect(request.getStreamUrl());
        droneRepository.save(drone);

        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    // 드론 상태 조회
    @Transactional(readOnly = true)
    public DroneStatusResponse getStatus() {
        Drone drone = droneRepository.findByStatus(DroneConnectionStatus.CONNECTED)
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    // 스트림 URL 조회 (프론트가 영상 표시할 때 사용)
    @Transactional(readOnly = true)
    public StreamUrlResponse getStreamUrl() {
        Drone drone = droneRepository.findByStatus(DroneConnectionStatus.CONNECTED)
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        return new StreamUrlResponse(drone.getStreamUrl());
    }

    // 드론 연결 해제
    @Transactional
    public void disconnect(Long droneId) {
        Drone drone = droneRepository.findById(droneId)
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        drone.disconnect();
        droneRepository.save(drone);
    }
}