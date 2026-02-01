package com.optify.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_simulation_details")
public class CartSimulationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_id")
    private CartSimulation simulation;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private double price;
    private String storeNames;
    private boolean isTransactional;

    public CartSimulationDetail() {
    }

    public CartSimulationDetail(Product product, double price, String storeNames, boolean isTransactional) {
        this.product = product;
        this.price = price;
        this.storeNames = storeNames;
        this.isTransactional = isTransactional;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CartSimulation getSimulation() {
        return simulation;
    }

    public void setSimulation(CartSimulation simulation) {
        this.simulation = simulation;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStoreNames() {
        return storeNames;
    }

    public void setStoreNames(String storeNames) {
        this.storeNames = storeNames;
    }

    public boolean isTransactional() {
        return isTransactional;
    }

    public void setTransactional(boolean transactional) {
        isTransactional = transactional;
    }
}
