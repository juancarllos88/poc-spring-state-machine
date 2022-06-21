package com.statemachine.incubator.example.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record OutputTrafficLightDto(Long id, String address, LocalDate installedAt, TrafficStatus state) {

    public static OutputTrafficLightDto from(TrafficLight trafficLight) {
        return new OutputTrafficLightDto(trafficLight.getId(), trafficLight.getAddress(), trafficLight.getInstalledAt(), trafficLight.getStatus());
    }
}