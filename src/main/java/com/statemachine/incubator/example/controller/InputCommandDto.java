package com.statemachine.incubator.example.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.state_machine.TrafficLightCommandType;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record InputCommandDto(TrafficLightCommandType command) {
    @JsonCreator
    public InputCommandDto {}
}