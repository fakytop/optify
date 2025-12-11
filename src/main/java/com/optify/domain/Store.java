/*
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
 */

package com.optify.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long rut;
    private String name; // Nombre Legal
    private String fantasyName; // Nombre Comercial o Fantasía
    private String homePage; // URL base

    @ElementCollection
    @CollectionTable(name = "store_category_urls", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "url_category")
    private List<String> urlCategory = new ArrayList<>();

    // Constructores
    public Store() {}
    public Store(String name) { this.name = name; }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public long getRut() { return rut; }
    public void setRut(long rut) { this.rut = rut; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFantasyName() { return fantasyName; }
    public void setFantasyName(String fantasyName) { this.fantasyName = fantasyName; }

    public String getHomePage() { return homePage; }
    public void setHomePage(String homePage) { this.homePage = homePage; }

    // metdo que usa tu lógica de consola
    public void addUrlCategory(String category) {
        if (this.urlCategory == null) {
            this.urlCategory = new ArrayList<>();
        }
        if (!this.urlCategory.contains(category)) {
            this.urlCategory.add(category);
        }
    }

    public List<String> getUrlCategory() {
        return urlCategory;
    }

    public void setUrlCategory(List<String> urlCategory) {
        this.urlCategory = urlCategory;
    }
}