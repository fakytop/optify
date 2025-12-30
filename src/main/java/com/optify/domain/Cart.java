package com.optify.domain;

import com.optify.exceptions.DataException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "cart", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_ci", referencedColumnName = "ci")
    private User user;

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addItem(CartItem item) {
        CartItem cartItem = getItem(item);
        if(cartItem == null) {
            this.items.add(item);
            return;
        }
        cartItem.setQuant(cartItem.getQuant() + item.getQuant());
    }

    private CartItem getItem(CartItem item) {
        for(CartItem cartItem : items) {
            if(cartItem.equals(item)) {
                return cartItem;
            }
        }
        return null;
    }

    public void removeItem(CartItem item) throws DataException {
        if(!items.contains(item)) {
            throw new DataException("[DataException] El producto ean: " + item.getEanProduct() + "ya no se encuentra en el carrito.");
        }

        this.items.remove(item);
    }
}
