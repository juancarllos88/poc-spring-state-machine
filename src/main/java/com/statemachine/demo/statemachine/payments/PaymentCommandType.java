package com.statemachine.demo.statemachine.payments;

public enum PaymentCommandType {
    TO_PAY, PAYMENT_IS_FRAUD, PAYMENT_AUTHORIZED, PAYMENT_NOT_AUTHORIZED, CLOSE;
}
