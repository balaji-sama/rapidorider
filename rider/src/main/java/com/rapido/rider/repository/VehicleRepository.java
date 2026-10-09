package com.rapido.rider.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rapido.rider.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

}
