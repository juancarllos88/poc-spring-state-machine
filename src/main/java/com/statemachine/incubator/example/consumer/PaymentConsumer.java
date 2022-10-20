package com.statemachine.incubator.example.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

    @RabbitListener(queues = {"${payments.broker.queue.email}"})
    public void receiveEmail(@Payload String payload){
        log.info(String.format("Listener Queue email: %s", payload));
    }

    @RabbitListener(queues = {"${payments.broker.queue.fraud}"})
    public void receiveFraud(@Payload String payload){
        log.info(String.format("Listener Queue fraud: %s", payload));
    }

    @RabbitListener(queues = {"${payments.broker.queue.error}"})
    public void receiveError(@Payload String payload){
        log.info(String.format("Listener Queue error: %s", payload));
    }
}
