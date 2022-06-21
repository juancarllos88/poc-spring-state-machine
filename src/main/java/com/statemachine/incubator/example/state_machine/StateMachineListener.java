package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.repository.TrafficLightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.OnEventNotAccepted;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransitionEnd;
import org.springframework.statemachine.annotation.WithStateMachine;

@WithStateMachine(id = "trafficLightListenner")
public class StateMachineListener {

    private static final Logger LOG = LoggerFactory.getLogger(StateMachineListener.class);

    @Autowired
    private TrafficLightRepository repository;

    /**
     * 2 formas diferentes de fazer:
     * - A primeira (syncronizeStateMachineWithEntity) faz o sync da status machine com a entity e já grava no BD
     * - A segunda (syncronizeStateMachineWithEntity) apenas faz o sync da status machine com a entity, porém deixa para o service persistir
     */

    @OnTransitionEnd(source = {"FLASHING_YELLOW", "RED", "YELLOW", "GREEN", "TURNED_OFF"})
    public void syncronizeStateMachineWithEntityInTheDatabase(StateContext context) {
        TrafficLight trafficLightFromStateMachine = context.getExtendedState().get("trafficLight", TrafficLight.class);
        trafficLightFromStateMachine.changeStateBasedOn(context.getStateMachine());
        var trafficLightFromDatabase = repository.save(trafficLightFromStateMachine);
        context.getExtendedState().getVariables().put("trafficLight", trafficLightFromDatabase);
    }

    @OnStateChanged(source = "TURNED_ON", target = "FLASHING_YELLOW")
    public void syncronizeStateMachineWithEntity(StateContext context) {
        LOG.info("State Changed from TURNED_ON to FLASHING_YELLOW");

        TrafficLight trafficLightFromStateMachine = context.getExtendedState().get("trafficLight", TrafficLight.class);

        if (trafficLightFromStateMachine != null) {
            trafficLightFromStateMachine.changeStateBasedOn(context.getStateMachine());
            context.getExtendedState().getVariables().put("trafficLight", trafficLightFromStateMachine);
        }
    }

    @OnEventNotAccepted
    public void eventNotAccepted(StateContext context) {
        LOG.warn("Event not accepted -> {} for current state {}", context.getEvent(), context.getStateMachine().getState().getId());
    }
}
