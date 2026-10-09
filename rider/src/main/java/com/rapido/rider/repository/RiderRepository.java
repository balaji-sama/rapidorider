package com.rapido.rider.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rapido.rider.entity.Rider;

public interface RiderRepository extends JpaRepository<Rider, Integer> {

}
