package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.Payment;
import com.statemachine.incubator.example.entity.PaymentStates;
import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.entity.TrafficLight.TrafficStatus;
import com.statemachine.incubator.example.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentMachineFactory {

    private final StateMachineFactory<PaymentStates, PaymentEvents> stateMachineFactory;

    @Autowired
    public PaymentMachineFactory(StateMachineFactory<PaymentStates, PaymentEvents> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public StateMachine<PaymentStates, PaymentEvents> createStateMachineWith(Payment payment) {
        var stateMachine = stateMachineFactory.getStateMachine();

        stateMachine.getExtendedState().getVariables().put("payment", payment);

        return stateMachine;
    }


    public StateMachine<PaymentStates, PaymentEvents> createStateMachineBasedOn(Payment payment) {

        var stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.stop();

        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
            var stateMachineContext = new DefaultStateMachineContext<PaymentStates, PaymentEvents>(
                    payment.getState(),
                    null,
                    null,
                    new DefaultExtendedState(Map.of("payment", payment)),
                    null,
                    stateMachine.getId());
            sma.resetStateMachine(stateMachineContext);
        });

        stateMachine.start();

        return stateMachine;
    }
}
