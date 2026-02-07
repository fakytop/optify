package com.optify.dto;

import com.optify.domain.Store;

public class StoreDto {
    private long rut;
    private String name;
    private String fantasyName;
    private String homePage;

    public StoreDto() {
    }

    public StoreDto(Store store) {
        this.rut = store.getRut();
        this.name = store.getName();
        this.fantasyName = store.getFantasyName();
        this.homePage = store.getHomePage();
    }

    public long getRut() {
        return rut;
    }

    public void setRut(long rut) {
        this.rut = rut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }
}
