package com.guseok.guseokbackend.drone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.dto.StreamUrlRequest;
import com.guseok.guseokbackend.drone.dto.StreamUrlResponse;
import com.guseok.guseokbackend.drone.service.DroneService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drone")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    // 맥북에서 스트림 URL 등록
    @PostMapping("/stream-register")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> registerStreamUrl(
        @Valid @RequestBody StreamUrlRequest request) {
        return ResponseEntity.ok(ApiResponse.success(droneService.registerStreamUrl(request)));
    }

    // 드론 상태 조회
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> getStatus() {
        return ResponseEntity.ok(ApiResponse.success(droneService.getStatus()));
    }

    // 스트림 URL 조회 (프론트가 영상 표시할 때)
    @GetMapping("/stream-url")
    public ResponseEntity<ApiResponse<StreamUrlResponse>> getStreamUrl() {
        return ResponseEntity.ok(ApiResponse.success(droneService.getStreamUrl()));
    }

    // 드론 연결 해제
    @DeleteMapping("/{droneId}/disconnect")
    public ResponseEntity<ApiResponse<Void>> disconnect(@PathVariable Long droneId) {
        droneService.disconnect(droneId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}