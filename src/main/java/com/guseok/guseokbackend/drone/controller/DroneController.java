package com.guseok.guseokbackend.drone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.service.DroneService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    // 사용 가능한 드론 목록 조회
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> getAvailable() {
        return ResponseEntity.ok(ApiResponse.success(droneService.getAvailable()));
    }
}