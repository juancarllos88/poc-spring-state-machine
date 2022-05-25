package com.statemachine.demo.controller;

import com.statemachine.demo.statemachine.RemoveCartItem;

public class RemoveCartItemDTO {
    private Long productId;
    private Long cartId;

    public RemoveCartItemDTO(Long productId, Long cartId) {
        this.productId = productId;
        this.cartId = cartId;
    }

    public RemoveCartItem toCommand() {
       return new RemoveCartItem(productId, cartId);
   }
}
