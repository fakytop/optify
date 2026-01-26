package com.optify.dto;

import com.optify.domain.Product;

public class ProductCatalogDto {

    private long productId;
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productBrand;
    private String categoryName;


    public ProductCatalogDto() {
    }

    public ProductCatalogDto(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.productDescription = product.getDescription();
        this.productImageUrl = product.getImageUrl();
        this.productBrand = product.getBrand();
        this.categoryName = product.getCategory().getName();
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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
}
