package com.statemachine.demo.statemachine;

public class RemoveCartItem implements CartCommand {
    private final Long productId;
    private final Long cartId;

    public RemoveCartItem(Long productId, Long cartId) {
        this.productId = productId;
        this.cartId = cartId;
    }

    @Override
    public CartCommandType getType() {
        return CartCommandType.REMOVE_ITEM;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCartId() {
        return cartId;
    }
}
