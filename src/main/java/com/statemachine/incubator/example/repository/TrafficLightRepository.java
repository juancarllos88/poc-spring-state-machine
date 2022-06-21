package com.statemachine.incubator.example.repository;

import com.statemachine.incubator.example.entity.TrafficLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficLightRepository extends JpaRepository<TrafficLight, Long> {}
