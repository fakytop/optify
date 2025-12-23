package com.optify.dto;

import com.optify.domain.Product;

public class ProductDto {
    private String productEan;
    private String productName;
    private String productGtin;
    private String productDescription;
    private String productImageUrl;
    private String productBrand;
    private String categoryName;
    private String categoryDescription;
    private long storeRut;
    private String urlProduct;
    private double productPrice;

    public ProductDto() {
    }

    public ProductDto(Product product) {
        this.productEan = product.getEan();
        this.productName = product.getName();
        this.productGtin = product.getGtin();
        this.productDescription = product.getDescription();
        this.productImageUrl = product.getImageUrl();
        this.productBrand = product.getBrand();
        this.categoryName = product.getCategory().getName();
        this.categoryDescription = product.getCategory().getDescription();
    }

    public String getProductEan() {
        return productEan;
    }

    public void setProductEan(String productEan) {
        this.productEan = productEan;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductGtin() {
        return productGtin;
    }

    public void setProductGtin(String productGtin) {
        this.productGtin = productGtin;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
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
