package com.statemachine.demo.statemachine;

import com.statemachine.demo.CartState;
import com.statemachine.demo.entity.Cart;
import com.statemachine.demo.entity.CartItem;
import com.statemachine.demo.repository.CartRepository;
import com.statemachine.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddNewAddToCartAction implements Action<CartState, CartCommandType> {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public void execute(StateContext<CartState, CartCommandType> context) {
        Cart cart = context.getExtendedState().get("cart", Cart.class);

        if (cart.getShippingPrice() != null && cart.getShippingPrice() > 0.0) {
            AddCartItem newItem = context.getExtendedState().get("newItem", AddCartItem.class);

            productRepository.findById(newItem.getProductId())
                .ifPresent(product -> {
                    cart.setState(context.getStateMachine().getState().getId());

                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(1);
                    cart.addNewItem(cartItem);

                    cartRepository.save(cart);
                });
        }
    }
}
