package com.guseok.guseokbackend.drone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.drone.dto.DroneStatusRequest;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.dto.StreamUrlRequest;
import com.guseok.guseokbackend.drone.service.DroneCallbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/drone-callback")
@RequiredArgsConstructor
public class DroneCallbackController {

    private final DroneCallbackService droneCallbackService;

    // 드론 → 백엔드 스트림 정보 등록
    // stream_server.py가 시작할 때 호출
    @PostMapping("/stream")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> registerStream(
        @Valid @RequestBody StreamUrlRequest request) {
        return ResponseEntity.ok(ApiResponse.success(droneCallbackService.registerStream(request)));
    }

    // 드론 → 백엔드 상태 업데이트
    // stream_server.py가 상태 변경 시 호출
    @PostMapping("/status")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> updateStatus(
        @Valid @RequestBody DroneStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(droneCallbackService.updateStatus(request)));
    }
}