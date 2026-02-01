package com.optify.dto;

import com.optify.domain.CartSimulation;
import com.optify.domain.CheapestProductInfo;

import java.util.ArrayList;
import java.util.List;

public class CartSimulationDto {

    private String name;
    private double totalCartValue;
    private double totalTransactionalCartValue;
    private List<CheapestProductInfo> products;

    public CartSimulationDto(CartSimulation sim) {
        this.name = sim.getName();
        this.totalCartValue = sim.getTotalCartValue();
        this.totalTransactionalCartValue = sim.getTotalTransactionalCartValue();
        this.products = new ArrayList<>(sim.getCheapestProducts().values());
    }

    public CartSimulationDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalCartValue() {
        return totalCartValue;
    }

    public void setTotalCartValue(double totalCartValue) {
        this.totalCartValue = totalCartValue;
    }

    public double getTotalTransactionalCartValue() {
        return totalTransactionalCartValue;
    }

    public void setTotalTransactionalCartValue(double totalTransactionalCartValue) {
        this.totalTransactionalCartValue = totalTransactionalCartValue;
    }

    public List<CheapestProductInfo> getProducts() {
        return products;
    }

    public void setProducts(List<CheapestProductInfo> products) {
        this.products = products;
    }
}
