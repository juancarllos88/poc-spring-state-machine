package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus.FLASHING_YELLOW;
import static com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus.GREEN;
import static com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus.RED;
import static com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus.TURNED_OFF;
import static com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus.TURNED_ON;
import static com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus.YELLOW;
import static com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus.values;
import static com.statemachine.incubator.example.state_machine.TrafficLightCommandType.CHANGE_TO_GREEN;
import static com.statemachine.incubator.example.state_machine.TrafficLightCommandType.CHANGE_TO_RED;
import static com.statemachine.incubator.example.state_machine.TrafficLightCommandType.CHANGE_TO_YELLOW;
import static com.statemachine.incubator.example.state_machine.TrafficLightCommandType.OUT_OF_SERVICE;
import static com.statemachine.incubator.example.state_machine.TrafficLightCommandType.START_SERVICE;
import static com.statemachine.incubator.example.state_machine.TrafficLightCommandType.TURN_OFF;

@Configuration
@EnableStateMachineFactory(contextEvents = false)
public class TrafficLightAdapterConfig extends EnumStateMachineConfigurerAdapter<TrafficStatus, TrafficLightCommandType> {

    private static Logger LOG = LoggerFactory.getLogger(TrafficLightAdapterConfig.class);

    @Override
    public void configure(StateMachineConfigurationConfigurer<TrafficStatus, TrafficLightCommandType> config) throws Exception {
        config.withConfiguration()
            .machineId("trafficLightListenner")
            .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<TrafficStatus, TrafficLightCommandType> states) throws Exception {
        states.withStates()
            .initial(TURNED_ON)
            .stateDo(TURNED_ON, initializeTrafficLight())
            .end(TURNED_OFF)
            .states(Arrays.stream(values()).collect(Collectors.toSet()));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TrafficStatus, TrafficLightCommandType> transitions) throws Exception {
        transitions.withExternal()
            .source(TURNED_ON).target(FLASHING_YELLOW).event(START_SERVICE)
        .and()
           .withExternal()
           .source(FLASHING_YELLOW).target(GREEN).event(CHANGE_TO_GREEN)
        .and()
            .withExternal()
            .source(GREEN).target(YELLOW).event(CHANGE_TO_YELLOW)
        .and()
            .withExternal()
            .source(YELLOW).target(RED).event(CHANGE_TO_RED)
        .and()
            .withExternal()
            .source(GREEN).target(FLASHING_YELLOW).event(OUT_OF_SERVICE)
        .and()
            .withExternal()
            .source(RED).target(FLASHING_YELLOW).event(OUT_OF_SERVICE)
        .and()
            .withExternal()
            .source(FLASHING_YELLOW).target(TURNED_OFF).event(TURN_OFF);
    }

    @Bean
    Action<TrafficStatus, TrafficLightCommandType> initializeTrafficLight() {
        return ctx -> {
            LOG.info("=================== > Initializing Traffic Light... < ===============================");

            ctx.getStateMachine().sendEvent(START_SERVICE);
        };
    }
}
