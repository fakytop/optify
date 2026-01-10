package com.optify.dto;

import com.optify.domain.CartItem;

public class ProductCartDto {
    private String productEan;
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productBrand;
    private String categoryName;
    private String categoryDescription;
    private double quant;

    public ProductCartDto() {
    }

    public ProductCartDto(CartItem cartItem) {
        this.productEan = cartItem.getProduct().getEan();
        this.productName = cartItem.getProduct().getName();
        this.productDescription = cartItem.getProduct().getDescription();
        this.productImageUrl = cartItem.getProduct().getImageUrl();
        this.productBrand = cartItem.getProduct().getBrand();
        this.categoryName = cartItem.getProduct().getCategory().getName();
        this.categoryDescription = cartItem.getProduct().getCategory().getDescription();
        this.quant = cartItem.getQuant();
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

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public double getQuant() {
        return quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }
}
