package com.statemachine.incubator.example.entity;

import com.statemachine.incubator.example.state_machine.PaymentEvents;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.statemachine.StateMachine;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

import static javax.persistence.EnumType.STRING;

@Entity
@DynamicUpdate
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PaymentStates status;

    @Column(nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "created_payday", updatable = false)
    private Instant createdPayDay;


    public Payment(Long id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
        this.status = PaymentStates.PAYMENT_STARTED;
    }

    public Payment() {

    }

    public void changeStateBasedOn(StateMachine<PaymentStates, PaymentEvents> stateMachine) {
        this.status = stateMachine.getState().getId();
    }
}
