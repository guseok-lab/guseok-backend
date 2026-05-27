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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Drone", description = "드론 연결 API")
@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
public class DroneSearchController {

    private final DroneSearchService droneSearchService;

    @Operation(
        summary = "드론 연결",
        description = "searchId에 해당하는 탐색에 드론을 연결합니다. 연결 성공 시 MJPEG 스트림 URL을 반환합니다."
    )
    @PostMapping("/{searchId}/drone/connect")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> connect(
        @PathVariable Long searchId) {
        return ResponseEntity.ok(ApiResponse.success(droneSearchService.connect(searchId)));
    }

    @Operation(
        summary = "드론 연결 해제",
        description = "searchId에 연결된 드론을 해제합니다."
    )
    @PostMapping("/{searchId}/drone/disconnect")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> disconnect(
        @PathVariable Long searchId) {
        return ResponseEntity.ok(ApiResponse.success(droneSearchService.disconnect(searchId)));
    }

    @Operation(
        summary = "드론 상태 조회",
        description = "searchId에 연결된 드론의 현재 상태와 스트림 URL을 조회합니다."
    )
    @GetMapping("/{searchId}/drone/status")
    public ResponseEntity<ApiResponse<DroneStatusResponse>> getStatus(
        @PathVariable Long searchId) {
        return ResponseEntity.ok(ApiResponse.success(droneSearchService.getStatus(searchId)));
    }
}