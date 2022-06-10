package com.statemachine.demo.entity;

import com.statemachine.demo.statemachine.payments.PaymentState;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "main_payment_status")
public class MainPaymentStatus {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "state_machine_id", nullable = false, columnDefinition = "VARCHAR(150)")
    @Type(type = "uuid-char")
    private UUID stateMachineId;

    @Column(name = "state", nullable = false)
    @Enumerated(STRING)
    private PaymentState state;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "main_payment_status_id")
    private List<SecondaryPaymentStatus> secondaryPaymentStatuses;

    /**
     *  @Deprecated - JPA Eyes
     */
    @Deprecated
    public MainPaymentStatus() {}

    public MainPaymentStatus(UUID stateMachineId, PaymentState state, List<SecondaryPaymentStatus> secondaryPaymentStatuses) {
        this.stateMachineId = stateMachineId;
        this.state = state;
        this.secondaryPaymentStatuses = secondaryPaymentStatuses;
    }

    public UUID getStateMachineId() {
        return stateMachineId;
    }

    public PaymentState getMainState() {
        return state;
    }

    public List<SecondaryPaymentStatus> getSecondaryPaymentStatuses() {
        return secondaryPaymentStatuses;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MainPaymentStatus.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("stateMachineId=" + stateMachineId)
                .add("state=" + state)
                .add("secondaryPaymentStatuses=" + secondaryPaymentStatuses)
                .toString();
    }
}
