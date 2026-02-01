package com.optify.domain;

import com.optify.exceptions.DataException;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CartSimulation {
    @Id
    private int id;
    private String name;
    private User user;
    private Timestamp date;
    HashMap<Integer,CheapestProductInfo> cheapestProducts;
    private double totalCartValue = 0;
    private double totalTransactionalCartValue = 0;

    public CartSimulation(String name, User user, Timestamp date) {
        this.name = name;
        this.user = user;
        this.date = date;
        this.cheapestProducts = new HashMap<>();
    }

    public CartSimulation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public HashMap<Integer, CheapestProductInfo> getCheapestProducts() {
        return cheapestProducts;
    }

    public void setCheapestProducts(HashMap<Integer, CheapestProductInfo> cheapestProducts) {
        this.cheapestProducts = cheapestProducts;
    }

    public void saveCheaperStoreProduct(CheapestProductInfo cheapestProductInfo) throws DataException {
        CheapestProductInfo actual = this.getCheapestProducts().get(cheapestProductInfo.getProduct().getId());
        if(actual == null) {
            this.getCheapestProducts().put(cheapestProductInfo.getProduct().getId(), cheapestProductInfo);
        } else if(actual.getPrice() > cheapestProductInfo.getPrice()) {
            this.getCheapestProducts().replace(cheapestProductInfo.getProduct().getId(), cheapestProductInfo);
        } else if(actual.getPrice() == cheapestProductInfo.getPrice()) {
            if(!cheapestProductInfo.getStores().isEmpty() && !actual.getStores().contains(cheapestProductInfo.getStores().getFirst())) {
                actual.addStore(cheapestProductInfo.getStores().getFirst());
            }
        }
    }

    public void setTotalValues(List<Integer> productIdsTransactional) {
        this.setTotalTransactionalCartValue(setTotalValue(productIdsTransactional,true));
        Set<Integer> allKeys = this.cheapestProducts.keySet();
        List<Integer> cheapestProductIds = allKeys.stream().toList();
        this.setTotalCartValue(setTotalValue(cheapestProductIds,false));
    }

    private double setTotalValue(List<Integer> productIdsTransactional, boolean isTransactional) {
        double total = 0;
        for(Integer productId : productIdsTransactional) {
            CheapestProductInfo product = this.getCheapestProducts().get(productId);
            product.setTransactional(isTransactional);
            total += product.getPrice();
        }
        return total;
    }

    public double getTotalCartValue() {
        return totalCartValue;
    }

    public double getTotalTransactionalCartValue() {
        return totalTransactionalCartValue;
    }

    public void setTotalCartValue(double totalCartValue) {
        this.totalCartValue = totalCartValue;
    }

    public void setTotalTransactionalCartValue(double totalTransactionalCartValue) {
        this.totalTransactionalCartValue = totalTransactionalCartValue;
    }
}
