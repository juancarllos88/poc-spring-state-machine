package com.statemachine.incubator.example.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachine.incubator.example.entity.Payment;
import com.statemachine.incubator.example.entity.PaymentStates;
import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record OutputPaymentDto(Long id, PaymentStates state, BigDecimal amount,  Instant createdPayDay) {

    public static OutputPaymentDto from(Payment payment) {
        return new OutputPaymentDto(payment.getId(), payment.getState(), payment.getAmount(), payment.getCreatedPayDay());
    }
}