//package com.statemachine.demo.controller;
//
//import com.statemachine.demo.statemachine.teste.Events;
//import com.statemachine.demo.statemachine.teste.States;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/tasks")
//public class TasksStateController {
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(TasksStateController.class);
//
//    @Autowired
//    private StateMachine<States, Events> stateMachine;
//
//    @PostMapping("/{event}")
//    public void sendEvent(@PathVariable Events event) {
//        stateMachine.sendEvent(event);
//
//        LOGGER.info("Estado atual -> {} ", stateMachine.getState());
//    }
//}
