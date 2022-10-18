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
    private PaymentStates state;

    @Column(nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "created_payday", updatable = false)
    private Instant createdPayDay;


    public Payment(Long id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
        this.state = PaymentStates.PAYMENT_STARTED;
    }

    public Payment() {

    }

    public void changeStateBasedOn(StateMachine<PaymentStates, PaymentEvents> stateMachine) {
        this.state = stateMachine.getState().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedPayDay() {
        return createdPayDay;
    }

    public void setCreatedPayDay(Instant createdPayDay) {
        this.createdPayDay = createdPayDay;
    }

    public PaymentStates getState() {
        return state;
    }

    public void setState(PaymentStates state) {
        this.state = state;
    }
}
