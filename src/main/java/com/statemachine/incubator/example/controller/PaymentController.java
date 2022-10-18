package com.statemachine.incubator.example.controller;

import com.statemachine.incubator.example.service.PaymentService;
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
@RequestMapping("/api/payments")
public class PaymentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<OutputPaymentDto> create(@RequestBody InputPaymentDto dto) {
        var payment = paymentService.create(dto.toEntity());
        return ResponseEntity.ok(OutputPaymentDto.from(payment));
    }

    @PatchMapping("/{paymentId}/statuses")
    public ResponseEntity<OutputPaymentDto> executeCommand(@PathVariable Long paymentId, @RequestBody InputCommandDto dto) {
        var payment = paymentService.execute(paymentId, dto.command());
        return ResponseEntity.ok(OutputPaymentDto.from(payment));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<OutputPaymentDto> findBy(@PathVariable Long paymentId) {
        return paymentService.findBy(paymentId)
                .map(payment -> ResponseEntity.ok(OutputPaymentDto.from(payment)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
