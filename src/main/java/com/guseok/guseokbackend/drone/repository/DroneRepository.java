package com.guseok.guseokbackend.drone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guseok.guseokbackend.entity.Drone;
import com.guseok.guseokbackend.entity.DroneStatus;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findFirstByStatusOrderByIdDesc(DroneStatus status);
    Optional<Drone> findBySearchId(Long searchId);
    Optional<Drone> findByDroneId(String droneId);
}