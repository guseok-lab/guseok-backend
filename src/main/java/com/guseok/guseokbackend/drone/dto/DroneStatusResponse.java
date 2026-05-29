package com.guseok.guseokbackend.drone.dto;

import com.guseok.guseokbackend.entity.DroneStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "드론 상태 응답")
public class DroneStatusResponse {

    @Schema(description = "드론 DB ID", example = "1")
    private final Long droneId;

    @Schema(description = "드론 연결 상태", example = "CONNECTED")
    private final DroneStatus status;

    @Schema(description = "MJPEG 스트림 URL", example = "https://xxx.trycloudflare.com/video/drone-a1b2c3d4")
    private final String streamUrl;
}