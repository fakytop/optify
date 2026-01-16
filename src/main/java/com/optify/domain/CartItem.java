package com.optify.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @EmbeddedId
    private CartItemPk id = new CartItemPk();

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private double quant;

    public CartItem() {
        this.id = new CartItemPk();
    }

    public CartItem(int id, long productId) {
        this.id = new CartItemPk(id,productId);
    }

    public CartItem(Cart cart, Product product, double quant) {
        this.cart = cart;
        this.product = product;
        this.quant = quant;
        this.id = new CartItemPk(cart.getId(),product.getId());
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        this.id.setCartId(cart.getId());
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.id.setProductId(product.getId());
    }

    public double getQuant() {
        return quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }

    public int getIdProduct() {
        return product.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem)obj;
        return id.equals(cartItem.id);
    }
}
