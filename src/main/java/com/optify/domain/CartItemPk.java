package com.optify.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartItemPk implements Serializable {

    private int cartId;
    private String productEan;

    public CartItemPk() {

    }

    public CartItemPk(int cartId, String productEan) {
        this.cartId = cartId;
        this.productEan = productEan;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartItemPk that = (CartItemPk) o;
        return cartId == that.cartId && that.productEan.equalsIgnoreCase(productEan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productEan);
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setProductEan(String productEan) {
        this.productEan = productEan;
    }
}