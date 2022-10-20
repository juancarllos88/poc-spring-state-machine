package com.statemachine.incubator.example.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentPublisher {
    private static final Logger log = LoggerFactory.getLogger(PaymentPublisher.class);

    @Value("${payments.broker.exchange}")
    private String exchange;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message, String routingKey) {
        log.info(String.format("Message sent -> %s", message));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
