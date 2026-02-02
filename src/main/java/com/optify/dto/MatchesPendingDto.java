package com.optify.dto;

import com.optify.domain.ManualMatchPending;

public class MatchesPendingDto {
    private int idMatchPending;
    private String productName;
    private int productBDId;
    private String productBDImage;
    private String storeProductUrl;
    private String storeBDName;
    private long storeBDRut;
    private long newProductIdWeb;
    private String newProductName;
    private String newPorductDescription;
    private String newProductImageUrl;
    private String newProductUrl;
    private double newProductPrice;

    public MatchesPendingDto() {
    }

    public MatchesPendingDto(ManualMatchPending manualMatchPending) {
        this.idMatchPending = manualMatchPending.getId();
        this.productName = manualMatchPending.getProduct().getName();
        this.productBDId = manualMatchPending.getProduct().getId();
        this.productBDImage = manualMatchPending.getProduct().getImageUrl();
        this.storeProductUrl = manualMatchPending.getUrlProductDB();
        this.storeBDName = manualMatchPending.getStore().getFantasyName();
        this.storeBDRut = manualMatchPending.getStore().getRut();
        this.newProductIdWeb = manualMatchPending.getIdWeb();
        this.newProductName = manualMatchPending.getProductName();
        this.newPorductDescription = manualMatchPending.getProductDescription();
        this.newProductImageUrl = manualMatchPending.getProductImageUrl();
        this.newProductUrl = manualMatchPending.getUrlProduct();
        this.newProductPrice = manualMatchPending.getProductPrice();
    }

    public int getIdMatchPending() {
        return idMatchPending;
    }

    public void setIdMatchPending(int idMatchPending) {
        this.idMatchPending = idMatchPending;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductBDId() {
        return productBDId;
    }

    public void setProductBDId(int productBDId) {
        this.productBDId = productBDId;
    }

    public String getProductBDImage() {
        return productBDImage;
    }

    public void setProductBDImage(String productBDImage) {
        this.productBDImage = productBDImage;
    }

    public String getStoreProductUrl() {
        return storeProductUrl;
    }

    public void setStoreProductUrl(String storeProductUrl) {
        this.storeProductUrl = storeProductUrl;
    }

    public String getStoreBDName() {
        return storeBDName;
    }

    public void setStoreBDName(String storeBDName) {
        this.storeBDName = storeBDName;
    }

    public long getStoreBDRut() {
        return storeBDRut;
    }

    public void setStoreBDRut(long storeBDRut) {
        this.storeBDRut = storeBDRut;
    }

    public long getNewProductIdWeb() {
        return newProductIdWeb;
    }

    public void setNewProductIdWeb(long newProductIdWeb) {
        this.newProductIdWeb = newProductIdWeb;
    }

    public String getNewProductName() {
        return newProductName;
    }

    public void setNewProductName(String newProductName) {
        this.newProductName = newProductName;
    }

    public String getNewPorductDescription() {
        return newPorductDescription;
    }

    public void setNewPorductDescription(String newPorductDescription) {
        this.newPorductDescription = newPorductDescription;
    }

    public String getNewProductImageUrl() {
        return newProductImageUrl;
    }

    public void setNewProductImageUrl(String newProductImageUrl) {
        this.newProductImageUrl = newProductImageUrl;
    }

    public String getNewProductUrl() {
        return newProductUrl;
    }

    public void setNewProductUrl(String newProductUrl) {
        this.newProductUrl = newProductUrl;
    }

    public double getNewProductPrice() {
        return newProductPrice;
    }

    public void setNewProductPrice(double newProductPrice) {
        this.newProductPrice = newProductPrice;
    }
}
