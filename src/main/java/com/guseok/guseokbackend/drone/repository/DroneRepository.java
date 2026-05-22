package com.guseok.guseokbackend.drone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guseok.guseokbackend.drone.entity.Drone;
import com.guseok.guseokbackend.drone.enums.DroneConnectionStatus;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findFirstByStatusOrderByIdDesc(DroneConnectionStatus status);
}