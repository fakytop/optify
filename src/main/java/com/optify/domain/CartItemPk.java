package com.optify.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartItemPk implements Serializable {

    private int cartId;
    private long productId;

    public CartItemPk() {

    }

    public CartItemPk(int cartId, long productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartItemPk that = (CartItemPk) o;
        return cartId == that.cartId && that.productId==productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productId);
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}