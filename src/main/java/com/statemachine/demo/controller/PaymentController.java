package com.statemachine.demo.controller;

import com.statemachine.demo.entity.Payment;
import com.statemachine.demo.service.PaymentService;
import com.statemachine.demo.statemachine.payments.PaymentCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;


    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto dto) {
        Payment payment = paymentService.createPayment(dto.toEntity());

        return ResponseEntity.ok(PaymentDto.from(payment));
    }

    @PatchMapping("{paymentId}/status/{event}")
    public ResponseEntity<PaymentDto> sendEvent(@PathVariable Long paymentId, @PathVariable PaymentCommandType event) {
        Payment payment = paymentService.changePaymentStatus(paymentId, event);

        return ResponseEntity.ok(PaymentDto.from(payment));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> get(@PathVariable Long paymentId) {
        return paymentService.findBy(paymentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
