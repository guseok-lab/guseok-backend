package com.guseok.guseokbackend.drone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.service.DroneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Drone", description = "드론 연결 API")
@RestController
@RequestMapping("/api/v1/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @Operation(
        summary = "사용 가능한 드론 조회",
        description = "현재 CONNECTED 상태인 드론을 조회합니다. 스트림 URL이 포함됩니다."
    )
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> getAvailable() {
        return ResponseEntity.ok(ApiResponse.success(droneService.getAvailable()));
    }
}