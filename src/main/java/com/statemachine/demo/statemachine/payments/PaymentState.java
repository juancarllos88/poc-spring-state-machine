package com.statemachine.demo.statemachine.payments;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public enum PaymentState {
    STARTED,
    IN_ANALYSIS,
    SERASA_IN_ANALYSIS,
    SERASA_APPROVED,
    SERASA_REPPROVED,
    CREDIT_CARD_IN_ANALYSIS,
    CREDIT_CARD_APPROVED,
    CREDIT_CARD_REPPROVED,
    JOIN_ANALYSIS,
    FRAUD,
    WITHOUT_FRAUD,
    AUTHORIZED,
    NOT_AUTHORIZED,
    FINISHED;

    public static Set<PaymentState> listIntermediaryStates() {
        return stream(PaymentState.values())
                .filter(state -> state != STARTED && state!= FINISHED)
                .collect(toSet());
    }
}
