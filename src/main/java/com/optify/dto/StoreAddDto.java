package com.optify.dto;

public class StoreAddDto {
    private long rut;
    private String name;
    private String fantasyName;
    private String homePage;

    public StoreAddDto() {
    }

    public StoreAddDto(long rut, String name, String fantasyName, String homePage) {
        this.rut = rut;
        this.name = name;
        this.fantasyName = fantasyName;
        this.homePage = homePage;
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

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
}
