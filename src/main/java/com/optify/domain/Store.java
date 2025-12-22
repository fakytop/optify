package com.optify.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
public class Store {
    @Id
    private long rut;
    private String name;
    private String fantasyName;
    private String homePage;
    @ElementCollection
    @CollectionTable(
            name = "store_categories_urls",
            joinColumns = @JoinColumn(name = "store_rut")
    )
    @Column(name = "category_url")
    private List<String> urlCategories = new ArrayList<>();

    @Override
    public String toString() {
        return "Store{" +
                "rut=" + rut +
                ", name='" + name + '\'' +
                ", fantasyName='" + fantasyName + '\'' +
                '}';
    }

    public List<String> getUrlCategories() {
        return urlCategories;
    }


    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
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

    public void setHomePage(String url) {
        this.homePage = url;
    }

    public void addUrlCategory(String category) {
        urlCategories.add(category);
    }
}
