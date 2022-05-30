package com.statemachine.demo.statemachine.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private StateMachine<PaymentState, PaymentCommandType> stateMachine;

    @PostMapping("/{event}")
    public void sendEvent(@PathVariable PaymentCommandType event) {
        stateMachine.sendEvent(event);

        LOGGER.info("Estado atual -> {} ", stateMachine.getState());
    }
}
