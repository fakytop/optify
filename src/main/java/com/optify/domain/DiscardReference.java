package com.optify.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "discarded_references")
public class DiscardReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DiscardReference(String url) {
        this.url = url;
    }

    public DiscardReference() {
    }
}
