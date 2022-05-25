package com.statemachine.demo.statemachine;

public class AddCartItem implements CartCommand {
    private final Long productId;
    private final Long cartId;

    public AddCartItem(Long productId, Long cartId) {
        this.productId = productId;
        this.cartId = cartId;
    }

    @Override
    public CartCommandType getType() {
        return CartCommandType.ADD_ITEM;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCartId() {
        return cartId;
    }
}
