package com.statemachine.incubator.example.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachine.incubator.example.entity.TrafficLight;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record InputTrafficLightDto(String address, LocalDate installedAt) {
    @JsonCreator
    public InputTrafficLightDto {}

    public TrafficLight toEntity() {
        return new TrafficLight(null, address, installedAt);
    }
}