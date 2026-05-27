package com.guseok.guseokbackend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "drones")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drone_name", nullable = false)
    private String droneName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneStatus status;

    private Integer battery;
    private Double latitude;
    private Double longitude;
    private Double altitude;

    // 스트리밍 관련 필드
    @Column(name = "drone_id", unique = true)
    private String droneId;         // drone-xxxxxxxx

    @Column(name = "stream_url")
    private String streamUrl;

    @Column(name = "search_id")
    private Long searchId;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Drone(
        String droneName, DroneStatus status,
        Integer battery, Double latitude,
        Double longitude, Double altitude
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

    // 스트리밍 연결
    public void connectStream(String droneId, String streamUrl, Long searchId) {
        this.droneId = droneId;
        this.streamUrl = streamUrl;
        this.searchId = searchId;
        this.status = DroneStatus.CONNECTED;
    }

    // 스트리밍 해제
    public void disconnectStream() {
        this.droneId = null;
        this.streamUrl = null;
        this.searchId = null;
        this.status = DroneStatus.DISCONNECTED;
    }

    // searchId 연동
    public void connectWithSearch(Long searchId) {
        this.searchId = searchId;
        this.status = DroneStatus.CONNECTED;
    }
}