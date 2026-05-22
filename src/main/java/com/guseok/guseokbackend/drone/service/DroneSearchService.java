package com.guseok.guseokbackend.drone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.entity.Drone;
import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;
import com.guseok.guseokbackend.drone.repository.DroneRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneSearchService {

    private final DroneRepository droneRepository;

    // 드론 연결 (searchId 연동)
    @Transactional
    public DroneStatusResponse connect(Long searchId) {
        Drone drone = droneRepository.findFirstByStatusOrderByIdDesc(DroneConnectionStatus.CONNECTED)
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        drone.connectWithSearch(drone.getStreamUrl(), searchId);
        droneRepository.save(drone);

        log.info("[드론] searchId {} 연결 완료", searchId);
        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    // 드론 연결 해제
    @Transactional
    public DroneStatusResponse disconnect(Long searchId) {
        Drone drone = droneRepository.findBySearchId(searchId)
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        drone.disconnect();
        droneRepository.save(drone);

        log.info("[드론] searchId {} 연결 해제", searchId);
        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    // 드론 상태 조회
    @Transactional(readOnly = true)
    public DroneStatusResponse getStatus(Long searchId) {
        Drone drone = droneRepository.findBySearchId(searchId)
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }
}