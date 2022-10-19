package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.Payment;
import com.statemachine.incubator.example.entity.PaymentStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.*;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Objects;
import java.util.Random;

@WithStateMachine(id = "paymentListener")
public class StateMachineListener extends StateMachineListenerAdapter<PaymentStates, PaymentEvents> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineListener.class);

    @OnTransitionEnd(target = {"IN_ANALYSIS","REJECTED_BY_FRAUD","AUTHORIZED", "CHECKING_AUTHORIZATION","NOT_AUTHORIZED"})
    public void syncStateMachineWithEntity(StateContext<PaymentStates, PaymentEvents> context) {
        log.info(String.format("-> State final from payment %s", context.getStateMachine().getState().getId()));
        var paymentFromStateMachine = context.getExtendedState().get("payment", Payment.class);

        if (Objects.nonNull(paymentFromStateMachine)) {

            paymentFromStateMachine.changeStateBasedOn(context.getStateMachine());

            context.getExtendedState()
                    .getVariables()
                    .put("payment", paymentFromStateMachine);
        }
    }
    @OnStateChanged()
    public void stateChanged(StateMachine<PaymentStates, PaymentEvents> stateMachine) {
        log.info(String.format("-> State Changed %s ", stateMachine.getState().getId().name()));
    }

    @OnTransitionStart(target = {"IN_ANALYSIS"})
    public void analyzingPayment(StateContext<PaymentStates, PaymentEvents> context) {
        Boolean analysis = new Random().nextBoolean();
        log.info(String.format("Starting analyze of fraud payment with status %s. Fraud: %s", context.getStateMachine().getState().getId(), analysis));
        context.getStateMachine()
                .getExtendedState()
                .getVariables()
                .put("isFraud", analysis);
    }

    @OnEventNotAccepted
    public void eventNotAccepted(StateContext<PaymentStates, PaymentEvents> context) {
        String messageError = String.format("Event (%s) not accepted for current state (%s)",
                context.getEvent(),
                context.getStateMachine().getState().getId().name());
        log.info(messageError);
        context.getExtendedState()
                .getVariables()
                .put("eventErrorMessage", messageError);
    }

}
