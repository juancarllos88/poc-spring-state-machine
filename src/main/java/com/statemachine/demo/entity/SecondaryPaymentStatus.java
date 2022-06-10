package com.statemachine.demo.entity;

import com.statemachine.demo.statemachine.payments.PaymentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.StringJoiner;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "secondary_payment_status")
public class SecondaryPaymentStatus {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "state", nullable = false)
    @Enumerated(STRING)
    private PaymentState state;

    /**
     *  @Deprecated - JPA Eyes
     */
    @Deprecated
    SecondaryPaymentStatus() {}

    public SecondaryPaymentStatus(Long id, PaymentState state) {
        this.id = id;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public PaymentState getSecondaryState() {
        return state;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SecondaryPaymentStatus.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("state=" + state)
                .toString();
    }
}
