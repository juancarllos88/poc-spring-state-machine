package com.statemachine.demo.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.statemachine.demo.statemachine.AddCartItem;

public class AddCartItemDTO {
    private Long productId;
    private Long cartId;

    @JsonCreator
    AddCartItemDTO(Long productId, Long cartId) {
        this.productId = productId;
        this.cartId = cartId;
    }

   public AddCartItem toCommand() {
       return new AddCartItem(productId, cartId);
   }

    public Long getProductId() {
        return productId;
    }

    public Long getCartId() {
        return cartId;
    }
}
