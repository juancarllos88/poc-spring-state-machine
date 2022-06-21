package com.statemachine.incubator.example.service;

import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.repository.TrafficLightRepository;
import com.statemachine.incubator.example.state_machine.TrafficLightCommandType;
import com.statemachine.incubator.example.state_machine.TrafficLightMachineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class TrafficLightService {

    @Autowired
    private TrafficLightRepository repository;
    @Autowired
    private TrafficLightMachineFactory factory;

    public Optional<TrafficLight> findBy(long id) {
        return repository.findById(id);
    }

    @Transactional
    public TrafficLight create(TrafficLight newTrafficLight) {
        var stateMachine = factory.createStateMachineWith(newTrafficLight);
        TrafficLight trafficLight = stateMachine.getExtendedState().get("trafficLight", TrafficLight.class);

        return repository.save(trafficLight);
    }

    @Transactional
    public TrafficLight execute(Long paymentId, TrafficLightCommandType command) {
        var stateMachine = factory.createStateMachineBasedOn(paymentId);
        stateMachine.sendEvent(command);

        var eventErrorMessage = stateMachine.getExtendedState().get("eventErrorMessage", String.class);
        Assert.state(eventErrorMessage == null, eventErrorMessage);

        return stateMachine.getExtendedState().get("trafficLight", TrafficLight.class);
    }
}
