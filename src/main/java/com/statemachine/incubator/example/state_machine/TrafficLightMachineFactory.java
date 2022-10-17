package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus;
import com.statemachine.incubator.example.repository.TrafficLightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrafficLightMachineFactory {

    @Autowired
    private StateMachineFactory<TrafficStatus, PaymentEvents> stateMachineFactory;

    @Autowired
    private TrafficLightRepository repository;

    public StateMachine<TrafficStatus, PaymentEvents> createStateMachineBasedOn(Long trafficLightId) {

        if (trafficLightId == null) {
            throw new IllegalArgumentException("O trafficLightId can NOT be null!");
        }

        var stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.stop();

        TrafficLight trafficLight = repository.findById(trafficLightId)
                .orElseThrow(()-> new IllegalArgumentException("Traffic Light not found -> " + trafficLightId));

        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
            StateMachineContext<TrafficStatus, PaymentEvents> stateMachineContext = new DefaultStateMachineContext<>(trafficLight.getStatus(), null, null, new DefaultExtendedState(Map.of("trafficLight", trafficLight)), null, stateMachine.getId());
            sma.resetStateMachine(stateMachineContext);
        });

        stateMachine.start();

        return stateMachine;
    }

    public StateMachine<TrafficStatus, PaymentEvents> createStateMachineWith(TrafficLight trafficLight) {
        var stateMachine = stateMachineFactory.getStateMachine();

        stateMachine.getExtendedState().getVariables().put("trafficLight", trafficLight);

        return stateMachine;
    }
}
