package com.statemachine.demo.statemachine.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class StateMachineListenerWithInterface extends StateMachineListenerAdapter<PaymentState, PaymentCommandType> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineListenerWithAnnotation.class);

    @Override
    public void stateMachineStarted(StateMachine stateMachine) {
        LOGGER.info("State Machine Started: {}", stateMachine.getUuid());
    }

    @Override
    public void stateChanged(State from, State to) {
        LOGGER.info("Transitioned from {} to {}", (from == null ? "none" : from.getId()), to.getId());
    }

    @Override
    public void stateEntered(State state) {
        LOGGER.info("State Entered -> {} ", state);
    }

    @Override
    public void stateExited(State state) {
        LOGGER.info("State Exit -> {} ", state);
    }

    @Override
    public void eventNotAccepted(Message event) {
        LOGGER.info("State Not Accepted -> {} ", event);
    }
}