package com.statemachine.demo.service;

import com.statemachine.demo.entity.Payment;
import com.statemachine.demo.repository.PaymentRepository;
import com.statemachine.demo.statemachine.payments.PaymentCommandType;
import com.statemachine.demo.statemachine.payments.PaymentState;
import com.statemachine.demo.statemachine.payments.PaymentStateMachineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;
    @Autowired
    private PaymentStateMachineFactory factory;

    public final Optional<Payment> findBy(long id) {
        return repository.findById(id);
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        var stateMachine = factory.createStateMachine();
        payment.changeStateBasedOn(stateMachine);

        repository.save(payment);

        return payment;
    }

    public Payment changePaymentStatus(Long paymentId, PaymentCommandType paymentCommandType) {
        var stateMachine = factory.createStateMachineBasedOn(paymentId);
        stateMachine.sendEvent(paymentCommandType);

        return stateMachine.getExtendedState().get("payment", Payment.class);
    }
}
