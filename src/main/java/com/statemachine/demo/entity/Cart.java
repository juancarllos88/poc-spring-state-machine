package com.statemachine.demo.entity;

import com.statemachine.demo.CartState;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.statemachine.demo.CartState.EMPTY;
import static javax.persistence.EnumType.STRING;

@Entity
public class Cart {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private List<CartItem> itens;

    @Column(name = "created_at", columnDefinition = "DATE")
    private LocalDate createtAt;

    @Enumerated(STRING)
    private CartState state = EMPTY;

    @Column(name = "shipping_price")
    private Double shippingPrice;

    public void addNewItem(CartItem cartItem) {

        if (itens == null) {
            itens = new ArrayList<>();
        }

        itens.add(cartItem);
    }

    public boolean isEmpty() {
        return itens.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public List<CartItem> getItens() {
        return itens;
    }

    public void setItens(List<CartItem> itens) {
        this.itens = itens;
    }

    public LocalDate getCreatetAt() {
        return createtAt;
    }

    public void setCreatetAt(LocalDate createtAt) {
        this.createtAt = createtAt;
    }

    public CartState getState() {
        return state;
    }

    public void setState(CartState state) {
        this.state = state;
    }
}
