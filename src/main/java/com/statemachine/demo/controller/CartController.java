package com.statemachine.demo.controller;

import com.statemachine.demo.CartState;
import com.statemachine.demo.repository.CartRepository;
import com.statemachine.demo.statemachine.AddCartItem;
import com.statemachine.demo.statemachine.CartCommandType;
import com.statemachine.demo.statemachine.RemoveCartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartRepository repository;
    @Autowired
    private StateMachine<CartState, CartCommandType> stateMachine;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void addCartItem(@RequestBody AddCartItemDTO dto) {
        AddCartItem command = dto.toCommand();

        repository.findById(command.getProductId())
            .ifPresent(cart -> stateMachine.getExtendedState().getVariables().put("cart", cart));
        stateMachine.sendEvent(CartCommandType.ADD_ITEM);
        System.out.println(stateMachine.getState());
    }

    @DeleteMapping
    public void removeCartItem(@RequestBody RemoveCartItemDTO dto) {
        RemoveCartItem command = dto.toCommand();

        repository.findById(command.getProductId())
                .ifPresent(cart -> stateMachine.getExtendedState().getVariables().put("cart", cart));

        stateMachine.sendEvent(CartCommandType.REMOVE_ITEM);
    }
}
