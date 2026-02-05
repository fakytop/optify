package com.optify.dto;

import com.optify.domain.StoreProduct;

public class StoreProductDto {
    private long storeRut;
    private String storeFantasyName;
    private double price;
    private String urlProduct;
    private long idWeb;
    private int productId;

    public StoreProductDto(StoreProduct storeProduct) {
        this.storeRut = storeProduct.getStoreRut();
        this.productId = storeProduct.getProduct().getId();
        this.storeFantasyName = storeProduct.getStore().getFantasyName();
        this.price = storeProduct.getPrice();
        this.urlProduct = storeProduct.getUrlProduct();
        this.idWeb = storeProduct.getIdWeb();
    }

    public StoreProductDto() {
    }

    public long getStoreRut() {
        return storeRut;
    }

    public void setStoreRut(long storeRut) {
        this.storeRut = storeRut;
    }

    public String getStoreFantasyName() {
        return storeFantasyName;
    }

    public void setStoreFantasyName(String storeFantasyName) {
        this.storeFantasyName = storeFantasyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public void setUrlProduct(String urlProduct) {
        this.urlProduct = urlProduct;
    }

    public long getIdWeb() {
        return idWeb;
    }

    public void setIdWeb(long idWeb) {
        this.idWeb = idWeb;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

}
