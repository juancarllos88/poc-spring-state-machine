package com.statemachine.demo.repository;

import com.statemachine.demo.entity.PaymentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentAnalysisRepository extends JpaRepository<PaymentAnalysis, Long> {

    List<PaymentAnalysis> findByPaymentId(Long paymentId);
}
