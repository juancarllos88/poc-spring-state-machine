package com.statemachine.incubator.example.entity;

import com.statemachine.incubator.example.state_machine.TrafficLightCommandType;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.statemachine.StateMachine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;

// Como a idéia é aprofundar na state machine, o modelo está simplificado

@Entity
@Table(name = "traffic_lights")
@DynamicUpdate
public class TrafficLight {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TrafficStatus status;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "date", name = "installed_at")
    private LocalDate installedAt;

    /**
     * @Deprecated - JPA Eyes
     */
    @Deprecated
    TrafficLight() {}

    public TrafficLight(Long id, String address, LocalDate installedAt) {
        this.id = id;
        this.address = address;
        this.installedAt = installedAt;
        this.status = TrafficStatus.TURNED_ON;
    }

    public void changeStateBasedOn(StateMachine<TrafficStatus, TrafficLightCommandType> stateMachine) {
        this.status = stateMachine.getState().getId();
    }

    public enum TrafficStatus {
        TURNED_ON, FLASHING_YELLOW, RED, YELLOW, GREEN, TURNED_OFF
    }

    public Long getId() {
        return id;
    }

    public TrafficStatus getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getInstalledAt() {
        return installedAt;
    }
}
