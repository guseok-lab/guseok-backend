package com.guseok.guseokbackend.drone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.service.DroneSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
public class DroneSearchController {

    private final DroneSearchService droneSearchService;

    // 드론 연결
    @PostMapping("/{searchId}/drone/connect")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> connect(
        @PathVariable Long searchId) {
        return ResponseEntity.ok(ApiResponse.success(droneSearchService.connect(searchId)));
    }

    // 드론 연결 해제
    @PostMapping("/{searchId}/drone/disconnect")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> disconnect(
        @PathVariable Long searchId) {
        return ResponseEntity.ok(ApiResponse.success(droneSearchService.disconnect(searchId)));
    }

    // 드론 상태 조회
    @GetMapping("/{searchId}/drone/status")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> getStatus(
        @PathVariable Long searchId) {
        return ResponseEntity.ok(ApiResponse.success(droneSearchService.getStatus(searchId)));
    }
}