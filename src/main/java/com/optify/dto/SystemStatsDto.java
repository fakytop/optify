package com.optify.dto;

public class SystemStatsDto {
    private long totalProducts;
    private long totalStores;
    private long totalUsers;
    private long totalCategories;
    private long totalStoreProducts;
    private long totalCartItems;
    private long totalPendingMatches;
    private long totalDiscardedReferences;

    public SystemStatsDto() {
    }

    public SystemStatsDto(long totalProducts, long totalStores, long totalUsers, long totalCategories,
                         long totalStoreProducts, long totalCartItems, long totalPendingMatches,
                         long totalDiscardedReferences) {
        this.totalProducts = totalProducts;
        this.totalStores = totalStores;
        this.totalUsers = totalUsers;
        this.totalCategories = totalCategories;
        this.totalStoreProducts = totalStoreProducts;
        this.totalCartItems = totalCartItems;
        this.totalPendingMatches = totalPendingMatches;
        this.totalDiscardedReferences = totalDiscardedReferences;
    }

    // Getters and Setters
    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalStores() {
        return totalStores;
    }

    public void setTotalStores(long totalStores) {
        this.totalStores = totalStores;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public long getTotalStoreProducts() {
        return totalStoreProducts;
    }

    public void setTotalStoreProducts(long totalStoreProducts) {
        this.totalStoreProducts = totalStoreProducts;
    }

    public long getTotalCartItems() {
        return totalCartItems;
    }

    public void setTotalCartItems(long totalCartItems) {
        this.totalCartItems = totalCartItems;
    }

    public long getTotalPendingMatches() {
        return totalPendingMatches;
    }

    public void setTotalPendingMatches(long totalPendingMatches) {
        this.totalPendingMatches = totalPendingMatches;
    }

    public long getTotalDiscardedReferences() {
        return totalDiscardedReferences;
    }

    public void setTotalDiscardedReferences(long totalDiscardedReferences) {
        this.totalDiscardedReferences = totalDiscardedReferences;
    }
}
