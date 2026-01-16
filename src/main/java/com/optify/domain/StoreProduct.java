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
    @MapsId("productId") //Mapea la columna de productEan de StoreProductPk
    @JoinColumn(name = "product_id") //Columna FK en la BD
    private Product product;

    private double price;
    private String urlProduct;
    private long idWeb;

    public long getIdWeb() {
        return idWeb;
    }

    public void setIdWeb(long idWeb) {
        this.idWeb = idWeb;
    }

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
        this.id.setProductId(product.getId());
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

    public StoreProductPk getId() {
        return this.id;
    }

    public long getStoreRut() {
        return this.id.getStoreRut();
    }

}
