package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "drone_locations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DroneLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 탐색 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_id", nullable = false)
    private Search search;

    // 드론 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    // 위도
    private Double latitude;

    // 경도
    private Double longitude;

    // 고도
    private Double altitude;

    // 기록 시각
    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Builder
    public DroneLocation(
        Search search,
        Drone drone,
        Double latitude,
        Double longitude,
        Double altitude,
        LocalDateTime recordedAt
    ) {
        this.search = search;
        this.drone = drone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.recordedAt = recordedAt;
    }
}
