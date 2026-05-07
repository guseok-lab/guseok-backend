package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "drones")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 드론 이름
    @Column(name = "drone_name", nullable = false)
    private String droneName;

    // AVAILABLE, CONNECTED, DISCONNECTED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneStatus status;

    // 배터리
    private Integer battery;

    // 위도
    private Double latitude;

    // 경도
    private Double longitude;

    // 고도
    private Double altitude;

    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Drone(
        String droneName,
        DroneStatus status,
        Integer battery,
        Double latitude,
        Double longitude,
        Double altitude
    ) {
        this.droneName = droneName;
        this.status = status;
        this.battery = battery;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public void updateStatus(DroneStatus status) {
        this.status = status;
    }

    public void updateLocation(Double latitude, Double longitude, Double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public void updateBattery(Integer battery) {
        this.battery = battery;
    }
}
