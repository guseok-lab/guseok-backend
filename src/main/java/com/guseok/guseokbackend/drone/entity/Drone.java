package com.guseok.guseokbackend.drone.entity;

import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;

import jakarta.persistence.Column;
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

    @Column(name = "drone_id", unique = true)
    private String droneId;     // drone-xxxxxxxx

    @Enumerated(EnumType.STRING)
    private DroneConnectionStatus status;

    private String streamUrl;

    private Long searchId;

    public static Drone create(String droneId, String streamUrl, Long searchId) {
        Drone drone = new Drone();
        drone.droneId = droneId;
        drone.streamUrl = streamUrl;
        drone.searchId = searchId;
        drone.status = DroneConnectionStatus.CONNECTED;
        return drone;
    }

    public void connect(String droneId, String streamUrl, Long searchId) {
        this.droneId = droneId;
        this.streamUrl = streamUrl;
        this.searchId = searchId;
        this.status = DroneConnectionStatus.CONNECTED;
    }

    public void connectWithSearch(String streamUrl, Long searchId) {
        this.status = DroneConnectionStatus.CONNECTED;
        this.streamUrl = streamUrl;
        this.searchId = searchId;
    }

    public void disconnect() {
        this.status = DroneConnectionStatus.DISCONNECTED;
        this.streamUrl = null;
        this.searchId = null;
    }
}