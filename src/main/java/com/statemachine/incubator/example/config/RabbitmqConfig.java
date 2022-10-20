package com.statemachine.incubator.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig {


    @Value("${payments.broker.queue.email}")
    private String emailQueue;

    @Value("${payments.broker.queue.fraud}")
    private String fraudQueue;

    @Value("${payments.broker.queue.error}")
    private String errorQueue;

    @Value("${payments.broker.exchange}")
    private String exchange;

    @Value("${payments.broker.routing-key.email}")
    private String emailRoutingKey;

    @Value("${payments.broker.routing-key.fraud}")
    private String fraudRoutingKey;

    @Value("${payments.broker.routing-key.error}")
    private String errorRoutingKey;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(emailQueue)
                .build();
    }

    @Bean
    public Binding bindingEmail() {
        return BindingBuilder.bind(emailQueue())
                .to(directExchange())
                .with(emailRoutingKey);
    }

    @Bean
    public Queue fraudQueue() {
        return QueueBuilder.durable(fraudQueue).build();
    }

    @Bean
    public Binding bindingFraud() {
        return BindingBuilder.bind(fraudQueue())
                .to(directExchange())
                .with(fraudRoutingKey);
    }

    @Bean
    public Queue errorQueue() {
        return QueueBuilder.durable(errorQueue)
                .build();
    }

    @Bean
    public Binding bindingError() {
        return BindingBuilder.bind(errorQueue())
                .to(directExchange())
                .with(errorRoutingKey);
    }

}
