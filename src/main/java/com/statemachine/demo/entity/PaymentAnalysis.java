package com.statemachine.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;

@Entity
public class PaymentAnalysis {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "analysis_type" )
    @Enumerated(STRING)
    private AnalysisType analysisType;

    private boolean approved;

    /**
     * @Deprecated - JPA Eyes
     */
    @Deprecated
    PaymentAnalysis() {}

    public PaymentAnalysis(Long paymentId, AnalysisType analysisType, boolean approved) {
        this.paymentId = paymentId;
        this.analysisType = analysisType;
        this.approved = approved;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    public boolean isApproved() {
        return approved;
    }

    public enum AnalysisType {
        SERASA, CREDIT_CARD
    }
}

