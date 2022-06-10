package com.statemachine.demo.statemachine.payments;

import com.statemachine.demo.entity.Payment;
import com.statemachine.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.OnTransitionEnd;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.transaction.annotation.Transactional;

@WithStateMachine(id = "paymentListenner")
public class StateMachineListenerWithAnnotation {

    @Autowired
    private PaymentRepository repository;

    @OnTransitionEnd
    @Transactional
    public void execute(StateContext context) {

        Payment payment = context.getExtendedState().get("payment", Payment.class);

        if (payment != null) {
            payment.changeStateBasedOn(context.getStateMachine());
            repository.save(payment);
        }
    }
}
