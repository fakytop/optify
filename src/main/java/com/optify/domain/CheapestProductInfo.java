package com.optify.domain;

import java.util.ArrayList;
import java.util.List;

public class CheapestProductInfo {
    private Product product;
    private List<Store> stores;
    private double price;
    private boolean transactional = false;

    public CheapestProductInfo() {

    }

    public CheapestProductInfo(Product product, double price) {
        this.product = product;
        this.stores = new ArrayList<>();
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addStore(Store store) {
        this.stores.add(store);
    }

    public boolean isTransactional() {
        return transactional;
    }

    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }
}
