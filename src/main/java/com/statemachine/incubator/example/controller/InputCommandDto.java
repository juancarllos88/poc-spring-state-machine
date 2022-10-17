package com.statemachine.incubator.example.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachine.incubator.example.state_machine.PaymentEvents;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record InputCommandDto(PaymentEvents command) {
    @JsonCreator
    public InputCommandDto {}
}