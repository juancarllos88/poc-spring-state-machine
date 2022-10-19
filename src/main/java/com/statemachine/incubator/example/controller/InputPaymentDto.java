package com.statemachine.incubator.example.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachine.incubator.example.entity.Payment;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record InputPaymentDto(BigDecimal amount) {
    @JsonCreator
    public InputPaymentDto {
    }

    public Payment toEntity() {
        return new Payment(null, amount);
    }
}