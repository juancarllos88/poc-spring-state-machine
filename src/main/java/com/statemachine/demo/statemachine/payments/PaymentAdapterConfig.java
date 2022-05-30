package com.statemachine.demo.statemachine.payments;

import com.statemachine.demo.statemachine.StateMachineListener;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.statemachine.demo.statemachine.payments.PaymentCommandType.TO_PAY;
import static com.statemachine.demo.statemachine.payments.PaymentState.CHECK_LIMIT_IN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.CREDIT_CARD_APPROVED;
import static com.statemachine.demo.statemachine.payments.PaymentState.CREDIT_CARD_IN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.CREDIT_CARD_REPPROVED;
import static com.statemachine.demo.statemachine.payments.PaymentState.IN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.JOIN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.SERASA_APPROVED;
import static com.statemachine.demo.statemachine.payments.PaymentState.SERASA_IN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.SERASA_REPPROVED;
import static com.statemachine.demo.statemachine.payments.PaymentState.STARTED;

@Component
@EnableStateMachine
public class PaymentAdapterConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentCommandType> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentCommandType> config) throws Exception {
        config.withConfiguration()
                .listener(new StateMachineListener())
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentCommandType> states) throws Exception {
        states.withStates()
            .initial(STARTED)
            .end(PaymentState.FINISHED)
            .fork(IN_ANALYSIS)
            .join(JOIN_ANALYSIS)
            .states(PaymentState.listIntermediaryStates())
            .and()
            .withStates()
                .parent(IN_ANALYSIS)
                .initial(SERASA_IN_ANALYSIS)
                .end(SERASA_APPROVED)
                .end(SERASA_REPPROVED)
            .and()
            .withStates()
                .parent(IN_ANALYSIS)
                .initial(CREDIT_CARD_IN_ANALYSIS)
                .end(CREDIT_CARD_APPROVED)
                .end(CREDIT_CARD_REPPROVED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentCommandType> transitions) throws Exception {
        transitions.withExternal()
            .source(STARTED).target(IN_ANALYSIS).event(TO_PAY)
        .and()
        .withFork()
            .source(IN_ANALYSIS)
            .target(SERASA_IN_ANALYSIS)
            .target(CREDIT_CARD_IN_ANALYSIS)
        .and()
        .withExternal()
                .

//            .withChoice()
//            .source(IN_ANALYSIS)
//            .first(PaymentState.FRAUD, fraudCheckerGuard())
//            .last(PaymentState.WITHOUT_FRAUD);
    }

    @Bean
    public Guard<PaymentState, PaymentCommandType> fraudCheckerGuard() {
        return ctx -> new Random().nextBoolean();
    }
}
