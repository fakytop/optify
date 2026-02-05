package com.optify.dto;

import com.optify.domain.Product;

public class ProductManualImportDto {
    private long idWeb;
    private int id;
    private long storeRut;
    private String urlProduct;
    private double productPrice;

    public ProductManualImportDto() {
    }

    public long getIdWeb() {
        return idWeb;
    }

    public void setIdWeb(long idWeb) {
        this.idWeb = idWeb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStoreRut() {
        return storeRut;
    }

    public void setStoreRut(long storeRut) {
        this.storeRut = storeRut;
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
