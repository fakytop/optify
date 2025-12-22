package com.optify.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
@Table(name = "store_products")
public class StoreProduct {
    //Clave compuesta
    @EmbeddedId
    private StoreProductPk id;

    //Relación con Store (Muchos StoreProduct a un Store)
    @ManyToOne
    @MapsId("storeRut") //Mapea la columna de storeRut de StoreProductPk
    @JoinColumn(name = "store_rut") //Columna FK en la BD
    private Store store;

    //Relación con Product (Muchos StoreProduct a un Product)
    @ManyToOne
    @MapsId("productEan") //Mapea la columna de productEan de StoreProductPk
    @JoinColumn(name = "product_ean") //Columna FK en la BD
    private Product product;

    private double price;
    private double lowPrice;
    private double highPrice;
    private Timestamp priceValidUntil;
    private String urlProduct;

    public StoreProduct() {
        this.id = new StoreProductPk();
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
        this.id.setProductEan(product.getEan());
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
        this.id.setStoreRut(store.getRut());
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

    public StoreProductPk getId() {
        return this.id;
    }

    public String getProductEan() {
        return this.id.getProductEan();
    }

    public long getStoreRut() {
        return this.id.getStoreRut();
    }
}
