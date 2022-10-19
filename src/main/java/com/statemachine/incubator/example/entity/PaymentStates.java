package com.statemachine.incubator.example.entity;

public enum PaymentStates {
    PAYMENT_STARTED,
    IN_ANALYSIS,
    REJECTED_BY_FRAUD,
    IN_AUTHORIZATION,
    AUTHORIZED,
    NOT_AUTHORIZED
}
