package com.statemachine.demo.entity;

import com.statemachine.demo.statemachine.payments.PaymentCommandType;
import com.statemachine.demo.statemachine.payments.PaymentState;
import org.springframework.statemachine.StateMachine;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal value;

    @Column(name = "payment_made_on_the_day", columnDefinition = "date")
    private LocalDate paymentMadeOnTheDay;

    @OneToOne(orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private MainPaymentStatus status;

    /**
     *  @Deprecated - JPA Eyes
     */
    @Deprecated
    Payment(){}

    public Payment(Long id, BigDecimal value, LocalDate paymentMadeOnTheDay, MainPaymentStatus status) {
        this.id = id;
        this.value = value;
        this.paymentMadeOnTheDay = paymentMadeOnTheDay;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public LocalDate getPaymentMadeOnTheDay() {
        return paymentMadeOnTheDay;
    }

    public PaymentState getState() {
        return status.getMainState();
    }

    public MainPaymentStatus getStatus() {
        return status;
    }


    public void changeStateBasedOn(StateMachine<PaymentState, PaymentCommandType> stateMachine) {
        List<PaymentState> ids = new ArrayList<>(stateMachine.getState().getIds());
        ids.remove(stateMachine.getState().getId());

        var secondaryStatuses = ids.stream()
            .map(subState -> new SecondaryPaymentStatus(null, subState))
            .toList();

        this.status = new MainPaymentStatus(stateMachine.getUuid(), stateMachine.getState().getId(), secondaryStatuses != null ? secondaryStatuses : new ArrayList<>());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Payment.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("value=" + value)
                .add("paymentMadeOnTheDay=" + paymentMadeOnTheDay)
                .add("status=" + status)
                .toString();
    }
}
