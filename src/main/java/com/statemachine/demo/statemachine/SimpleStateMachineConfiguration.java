//package com.statemachine.demo.statemachine;
//
//import com.statemachine.demo.CartState;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.statemachine.action.Action;
//import org.springframework.statemachine.config.EnableStateMachine;
//import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
//import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
//import org.springframework.stereotype.Component;
//
//import static com.statemachine.demo.CartState.CHECKED_OUT;
//import static com.statemachine.demo.CartState.EMPTY;
//import static com.statemachine.demo.CartState.ADD_TO_CART_CHOICE;
//import static com.statemachine.demo.CartState.READY_FOR_CHECKOUT;
//import static com.statemachine.demo.CartState.SHIPPIND_CALCULATION_FAILED;
//import static com.statemachine.demo.statemachine.CartCommandType.ADD_ITEM;
//
//@Component
//@EnableStateMachine
//public class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<CartState, CartCommandType>  {
//
//    private static final Logger LOG = LoggerFactory.getLogger(SimpleStateMachineConfiguration.class);
//
//    @Autowired
//    private EmptyGuard emptyGuard;
//
//    @Autowired
//    private AddItemGuard addItemGuard;
//
//    @Autowired
//    private AddNewAddToCartAction addNewAddToCartAction;
//
//    @Override
//    public void configure(StateMachineConfigurationConfigurer<CartState, CartCommandType> config) throws Exception {
//        config
//            .withConfiguration()
//            .autoStartup(true)
//            .listener(new StateMachineListener());
//    }
//
//    @Override
//    public void configure(StateMachineStateConfigurer<CartState, CartCommandType> states) throws Exception {
//        states.withStates()
//                .initial(EMPTY)
//                .end(CHECKED_OUT)
//                .choice(ADD_TO_CART_CHOICE)
//                .stateDo(READY_FOR_CHECKOUT, addNewAddToCartAction)
//                .states(CartState.getIntermediaryStates());
//    }
//
//    @Override
//    public void configure(StateMachineTransitionConfigurer<CartState, CartCommandType> transitions) throws Exception {
//        transitions
//            .withExternal()
//                .source(EMPTY).target(ADD_TO_CART_CHOICE).event(ADD_ITEM)
//            .and()
//            .withChoice()
//                .source(ADD_TO_CART_CHOICE)
//                .first(READY_FOR_CHECKOUT, addItemGuard)
//                .last(SHIPPIND_CALCULATION_FAILED);
//    }
//
//    @Bean
//    public Action<CartState, CartCommandType> addNewAddToCartAndSave() {
//        return ctx -> {
//            LOG.info("Init Action = Target ID: {}", ctx.getTarget().getId());
//
////            ctx.getExtendedState().put
//        };
//    }
//
//    @Bean
//    public Action<CartState, CartCommandType> errorAction() {
//        return ctx -> LOG.error("Error Action = Target ID: {}", ctx.getTarget().getId());
//    }
//
//    @Bean
//    public Action<CartState, CartCommandType> entryAction() {
//        return ctx -> {
//
////            if (1 == 1) {
////                throw new RuntimeException("bla");
////            }
//
//            System.out.println(
//                    "Entry " + ctx.getTarget().getId());
//        };
//    }
//
//    @Bean
//    public Action<CartState, CartCommandType> executeAction() {
//        return ctx -> {
//
////            if (1 == 1) {
////                throw new RuntimeException("bla");
////            }
//
//            System.out.println("Do " + ctx.getTarget().getId());
//        };
//    }
//
//    @Bean
//    public Action<CartState, CartCommandType> exitAction() {
//        return ctx -> {
//
////            if (1 == 1) {
////                throw new RuntimeException("bla");
////            }
//            System.out.println(
//                    "Exit " + ctx.getSource().getId() + " -> " + ctx.getTarget().getId());
//        };
//    }
//}
