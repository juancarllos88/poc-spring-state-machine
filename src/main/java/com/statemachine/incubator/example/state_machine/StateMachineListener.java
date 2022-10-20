package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.Payment;
import com.statemachine.incubator.example.entity.PaymentStates;
import com.statemachine.incubator.example.publisher.PaymentPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.*;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.Objects;
import java.util.Random;

@WithStateMachine(id = "paymentListener")
public class StateMachineListener {

    private static final Logger log = LoggerFactory.getLogger(StateMachineListener.class);

    @Value("${payments.broker.routing-key.error}")
    private String errorRoutingKey;

    @Autowired
    private PaymentPublisher publisher;

    @OnTransitionEnd(source = {"PAYMENT_STARTED", "IN_ANALYSIS", "IN_AUTHORIZATION", "AUTHORIZED", "NOT_AUTHORIZED", "REJECTED_BY_FRAUD"})
    public void syncStateMachineWithEntity(StateContext<PaymentStates, PaymentEvents> context) {
        var paymentFromStateMachine = context.getExtendedState().get("payment", Payment.class);

        if (Objects.nonNull(paymentFromStateMachine)) {

            paymentFromStateMachine.changeStateBasedOn(context.getStateMachine());

            context.getExtendedState()
                    .getVariables()
                    .put("payment", paymentFromStateMachine);
        }
    }


    @OnTransitionStart(target = {"IN_ANALYSIS"})
    public void analyzingPayment(StateContext<PaymentStates, PaymentEvents> context) {
        Boolean analysis = new Random().nextBoolean();
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
        context.getExtendedState()
                .getVariables()
                .put("eventErrorMessage", messageError);
        publisher.send(messageError, errorRoutingKey);
    }

}
