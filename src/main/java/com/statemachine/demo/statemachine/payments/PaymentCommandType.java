package com.statemachine.demo.statemachine.payments;

public enum PaymentCommandType {
    START_ANALYSIS, FINISH_SERASA_ANALYSIS, FINISH_CREDIT_CARD_ANALYSIS, EXECUTE_PAYMENT, FRAUD_DETECTED;
}
