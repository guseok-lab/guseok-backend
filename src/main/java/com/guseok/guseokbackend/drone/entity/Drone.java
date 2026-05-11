package com.guseok.guseokbackend.drone.entity;

import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "drone")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DroneConnectionStatus status;

    private String streamUrl;  // 맥북 MJPEG 스트림 URL

    public static Drone create() {
        Drone drone = new Drone();
        drone.status = DroneConnectionStatus.AVAILABLE;
        return drone;
    }

    public void connect(String streamUrl) {
        this.status = DroneConnectionStatus.CONNECTED;
        this.streamUrl = streamUrl;
    }

    public void disconnect() {
        this.status = DroneConnectionStatus.DISCONNECTED;
        this.streamUrl = null;
    }
}