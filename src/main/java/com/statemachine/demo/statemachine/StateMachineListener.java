package com.statemachine.demo.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class StateMachineListener extends StateMachineListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineListener.class);

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