package com.statemachine.demo.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachine.demo.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record PaymentDto(Long id, BigDecimal value, LocalDate paymentMadeOnTheDay) {
    @JsonCreator
    public PaymentDto {
    }

    public Payment toEntity() {
        return new Payment(null, value, paymentMadeOnTheDay, null);
    }

    public static PaymentDto from(Payment payment) {
        return new PaymentDto(payment.getId(), payment.getValue(), payment.getPaymentMadeOnTheDay());
    }
}
