package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.PaymentStates;
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
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;
import java.util.Objects;

import static com.statemachine.incubator.example.state_machine.PaymentEvents.*;
import static com.statemachine.incubator.example.entity.PaymentStates.*;

@Configuration
@EnableStateMachineFactory(contextEvents = false)
public class PaymentAdapterConfig extends EnumStateMachineConfigurerAdapter<PaymentStates, PaymentEvents> {

    private static final Logger log = LoggerFactory.getLogger(PaymentAdapterConfig.class);

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentStates, PaymentEvents> config) throws Exception {
        config.withConfiguration()
            .machineId("paymentListener")
            .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<PaymentStates, PaymentEvents> states) throws Exception {
        states.withStates()
            .initial(PAYMENT_STARTED)
                .stateDo(PAYMENT_STARTED, initializePayment())
                .stateDo(AUTHORIZED, sendStatusByEmail())
                .stateDo(NOT_AUTHORIZED, sendStatusByEmail())
                .stateDo(REJECTED_BY_FRAUD, sendStatusByEmail())
                .choice(IN_ANALYSIS)
                .end(REJECTED_BY_FRAUD)
                .end(AUTHORIZED)
                .end(NOT_AUTHORIZED)
                .states(EnumSet.allOf(PaymentStates.class));
    }



    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentStates, PaymentEvents> transitions) throws Exception {
        transitions.withExternal()
            .source(PAYMENT_STARTED).target(IN_ANALYSIS).event(ANALYZE)
        .and()
            .withChoice()
            .source(IN_ANALYSIS).first(REJECTED_BY_FRAUD, analyzePayment())
                                .last(IN_AUTHORIZATION)
        .and().withExternal()
            .source(IN_AUTHORIZATION).target(AUTHORIZED).event(AUTHORIZE)
        .and().withExternal()
            .source(IN_AUTHORIZATION).target(NOT_AUTHORIZED).event(NOT_AUTHORIZE);

    }

    @Bean
     Guard<PaymentStates, PaymentEvents> analyzePayment() {
        return stateContext ->{
            var isFraud = (Boolean) stateContext.getStateMachine()
                    .getExtendedState()
                    .getVariables()
                    .get("isFraud");
            log.info("-> Checking result from analyze from Payment");
            return Objects.isNull(isFraud) || isFraud;
        };
    }

    @Bean
    Action<PaymentStates, PaymentEvents> initializePayment() {
        return ctx -> {
            log.info("Initializing Payment...");
            //ctx.getStateMachine().sendEvent(START_PAYMENT);
        };
    }

    @Bean
    Action<PaymentStates, PaymentEvents> sendStatusByEmail() {
        return ctx -> {
            log.info("Sending email: actual state: " + ctx.getStateMachine().getState().getId().name());
        };
    }

}

