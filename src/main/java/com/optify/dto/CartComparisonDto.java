package com.optify.dto;

public class CartComparisonDto {
    private CartSimulationDto optimum;
    private CartSimulationDto cheapestStore;
    private CartSimulationDto preferedStore;

    public CartComparisonDto() {
    }

    public CartSimulationDto getOptimum() {
        return optimum;
    }

    public void setOptimum(CartSimulationDto optimum) {
        this.optimum = optimum;
    }

    public CartSimulationDto getCheapestStore() {
        return cheapestStore;
    }

    public void setCheapestStore(CartSimulationDto cheapestStore) {
        this.cheapestStore = cheapestStore;
    }

    public CartSimulationDto getPreferedStore() {
        return preferedStore;
    }

    public void setPreferedStore(CartSimulationDto preferedStore) {
        this.preferedStore = preferedStore;
    }
}
