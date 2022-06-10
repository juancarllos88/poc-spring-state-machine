package com.statemachine.demo.statemachine.payments;

import com.statemachine.demo.entity.Payment;
import com.statemachine.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PaymentStateMachineFactory {

    @Autowired
    private StateMachineFactory<PaymentState, PaymentCommandType> stateMachineFactory;

    @Autowired
    private PaymentRepository repository;

    public StateMachine<PaymentState, PaymentCommandType> createStateMachineBasedOn(Long paymentId) {

        if (paymentId == null) {
            throw new IllegalArgumentException("O paymentId can NOT be null!");
        }

        var stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.stop();

        Payment payment = repository.findPaymentByIdWithStatus(paymentId)
                .orElseThrow(()-> new IllegalArgumentException("Payment not found -> " + paymentId));

        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
            var stateMachineContext = new DefaultStateMachineContext<>(getSubstates(payment), payment.getState(), null, null, new DefaultExtendedState(Map.of("payment", payment)), null, stateMachine.getId());
            sma.resetStateMachine(stateMachineContext);
        });

        stateMachine.start();

        return stateMachine;
    }

    private List<StateMachineContext<PaymentState, PaymentCommandType>> getSubstates(Payment payment) {

        if (payment.getStatus() != null && payment.getStatus().getSecondaryPaymentStatuses() != null) {
            return payment.getStatus().getSecondaryPaymentStatuses()
                    .stream()
                    .map(state -> (StateMachineContext<PaymentState, PaymentCommandType>) new DefaultStateMachineContext<PaymentState, PaymentCommandType>(state.getSecondaryState(), null, null, null))
                    .toList();
        }

        return new ArrayList<>();
    }


    public StateMachine<PaymentState, PaymentCommandType> createStateMachine() {
        return stateMachineFactory.getStateMachine();
    }
}
