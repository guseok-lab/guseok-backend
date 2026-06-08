package com.guseok.guseokbackend.drone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.drone.dto.DroneDetectionRequest;
import com.guseok.guseokbackend.drone.dto.DroneStatusRequest;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.dto.StreamUrlRequest;
import com.guseok.guseokbackend.drone.service.DroneCallbackService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "DroneCallback", description = "드론 서버 연동 API")
@RestController
@RequestMapping("/api/v1/drone-callback")
@RequiredArgsConstructor
public class DroneCallbackController {

    private final DroneCallbackService droneCallbackService;

    @Operation(
        summary = "드론 스트림 URL 등록",
        description = "드론 서버(app.py)가 폰 연결 시 스트림 URL을 Spring에 등록합니다. 드론 서버 내부 API입니다."
    )
    @PostMapping("/stream")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> registerStream(
        @Valid @RequestBody StreamUrlRequest request) {
        return ResponseEntity.ok(ApiResponse.success(droneCallbackService.registerStream(request)));
    }

    @Operation(
        summary = "드론 상태 업데이트",
        description = "드론 서버(app.py)가 상태 변경 시 Spring에 알립니다. 드론 서버 내부 API입니다."
    )
    @PostMapping("/status")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> updateStatus(
        @Valid @RequestBody DroneStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(droneCallbackService.updateStatus(request)));
    }

    @Operation(
        summary = "드론 실시간 탐지 결과 수신",
        description = "AI 서버가 드론 스트림에서 탐지한 결과를 전송합니다. DB에 저장 후 WebSocket으로 프론트에 전달합니다."
    )
    @PostMapping("/detection")
    public ResponseEntity<ApiResponse<Void>> handleDetection(
        @Valid @RequestBody DroneDetectionRequest request) {
        droneCallbackService.handleDetection(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}