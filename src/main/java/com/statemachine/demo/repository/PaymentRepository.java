package com.statemachine.demo.repository;

import com.statemachine.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT p FROM Payment p LEFT JOIN p.status s LEFT JOIN s.secondaryPaymentStatuses WHERE p.id =:id")
    Optional<Payment> findPaymentByIdWithStatus(Long id);
}
