package com.statemachine.demo.statemachine;

import com.statemachine.demo.CartState;
import com.statemachine.demo.entity.Cart;
import com.statemachine.demo.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class EmptyGuard implements Guard<CartState, CartCommandType> {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public boolean evaluate(StateContext<CartState, CartCommandType> context) {
        Cart cart = context.getExtendedState().get("cart", Cart.class);

        return cart.isEmpty();
    }
}
