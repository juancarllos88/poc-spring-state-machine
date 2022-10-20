package com.statemachine.incubator.example.service;

import com.statemachine.incubator.example.entity.Payment;
import com.statemachine.incubator.example.repository.PaymentRepository;
import com.statemachine.incubator.example.state_machine.PaymentEvents;
import com.statemachine.incubator.example.state_machine.PaymentMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class PaymentService {

    private final static Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository repository;
    private final PaymentMachineFactory factory;

    @Autowired
    public PaymentService(PaymentRepository repository, PaymentMachineFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public Optional<Payment> findBy(long id) {
        return repository.findById(id);
    }

    @Transactional
    public Payment create(Payment payment) {
        var stateMachine = factory.createStateMachineWith(payment);
        Payment paymentFromStateMachine = stateMachine.getExtendedState().get("payment", Payment.class);
        return repository.save(paymentFromStateMachine);
    }

    @Transactional
    public Payment updateState(Long paymentId, PaymentEvents command) {
        var payment = repository.findById(paymentId)
                .orElseThrow(()-> new IllegalArgumentException("Payment not found -> " + paymentId));

        var stateMachine = factory.createStateMachineBasedOn(payment);
        stateMachine.sendEvent(command);

        var eventErrorMessage = stateMachine.getExtendedState().get("eventErrorMessage", String.class);
        Assert.state(eventErrorMessage == null, eventErrorMessage);

        var paymentFromStateMachine = stateMachine.getExtendedState().get("payment", Payment.class);
        return repository.save(paymentFromStateMachine);
    }
}
