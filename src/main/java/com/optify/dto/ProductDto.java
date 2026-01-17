package com.optify.dto;

import com.optify.domain.Product;

public class ProductDto {
    private long idWeb;
    private int productId;
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productBrand;
    private String categoryName;
    private long storeRut;
    private String urlProduct;
    private double productPrice;

    public ProductDto() {
    }

    public ProductDto(Product product) {
        this.idWeb = product.getId();
        this.productId = product.getId();
        this.productName = product.getName();
        this.productDescription = product.getDescription();
        this.productImageUrl = product.getImageUrl();
        this.productBrand = product.getBrand();
        this.categoryName = product.getCategory().getName();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
