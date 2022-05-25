package com.statemachine.demo;

import java.util.Set;

public enum CartState {
    EMPTY,
//    OPTIONAL_SHIPPING_BEFORE_CHECKOUT,
    SHIPPIND_CALCULATION_FAILED,
    READY_FOR_CHECKOUT,
    CHECKED_OUT,
    ADD_TO_CART_CHOICE;

    public static Set<CartState> getIntermediaryStates() {
        return Set.of(
//                OPTIONAL_SHIPPING_BEFORE_CHECKOUT,
                SHIPPIND_CALCULATION_FAILED,
                READY_FOR_CHECKOUT,
                ADD_TO_CART_CHOICE
        );
    }
}
