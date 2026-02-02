package com.optify.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "manual_match_pending", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"store_rut", "idWeb"})
})
public class ManualMatchPending {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    //Datos del JSON
    @ManyToOne
    @JoinColumn(name = "store_rut")
    private Store store;
    private long idWeb;
    private String productName;
    @Column(length = 30000)
    private String productDescription;
    @Column(length = 30000)
    private String productImageUrl;
    private String productBrand;
    private String categoryName;
    private String urlProduct;
    private String urlProductDB;
    private double productPrice;

    public ManualMatchPending() {
    }

    public ManualMatchPending(Product product, Store store, long idWeb, String productName, String productDescription, String productImageUrl, String productBrand, String categoryName, String urlProduct, double productPrice, String urlProductDB) {
        this.product = product;
        this.store = store;
        this.idWeb = idWeb;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productBrand = productBrand;
        this.categoryName = categoryName;
        this.urlProduct = urlProduct;
        this.productPrice = productPrice;
        this.urlProductDB = urlProductDB;
    }

    public int getId() {
        return id;
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

    public long getIdWeb() {
        return idWeb;
    }

    public void setIdWeb(long idWeb) {
        this.idWeb = idWeb;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public void setUrlProduct(String urlProduct) {
        this.urlProduct = urlProduct;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
