package com.optify.domain;

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
        this.items.add(item);
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
    }
}
