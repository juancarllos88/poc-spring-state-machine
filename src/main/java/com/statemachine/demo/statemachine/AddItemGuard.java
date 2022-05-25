package com.statemachine.demo.statemachine;

import com.statemachine.demo.CartState;
import com.statemachine.demo.entity.Cart;
import com.statemachine.demo.service.ShippingCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddItemGuard implements Guard<CartState, CartCommandType> {

    @Autowired
    private ShippingCalculator shippingCalculator;

    @Override
    public boolean evaluate(StateContext<CartState, CartCommandType> context) {
        Cart cart = context.getExtendedState().get("cart", Cart.class);

        return shippingCalculator.calculate()
          .map(shippingValue -> {
              cart.setShippingPrice(shippingValue);
              return true;
          }).orElse(false);
    }
}
