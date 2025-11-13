package com.optify.domain;

import java.sql.Timestamp;

public class StoreProduct {
    private Product product;
    private Store store;
    private double price;
    private double lowPrice;
    private double highPrice;
    private Timestamp priceValidUntil;
    private String urlProduct;

    public StoreProduct() {
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public void setUrlProduct(String urlProduct) {
        this.urlProduct = urlProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public Timestamp getPriceValidUntil() {
        return priceValidUntil;
    }

    public void setPriceValidUntil(Timestamp priceValidUntil) {
        this.priceValidUntil = priceValidUntil;
    }
}
