//package com.statemachine.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StateService {
//
//    @Autowired
//    private StateMachine<CartState, CartCommand> stateMachine;
//
//    @Autowired
//    private GlobalListener listener;
//
//    public void execute() {
////        stateMachine.start();
////        stateMachine.addStateListener(listener);
////
////        stateMachine.sendEvent(CartCommand.ADD_TO_CART);
////        stateMachine.sendEvent(REGISTER_FEEDBACK); // Ignorou
////
////        System.out.println(stateMachine.getState());
//    }
//}
