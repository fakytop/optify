package com.optify.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StoreProductPk implements Serializable {
    private long storeRut;
    private String productEan;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        StoreProductPk that = (StoreProductPk) o;
        return storeRut == that.storeRut && productEan.equals(that.productEan);
    }

    @Override
    public int hashCode() {
        return  Objects.hash(storeRut, productEan);
    }

    public long getStoreRut() {
        return storeRut;
    }

    public String getProductEan() {
        return productEan;
    }

    public void setStoreRut(long storeRut) {
        this.storeRut = storeRut;
    }

    public void setProductEan(String productEan) {
        this.productEan = productEan;
    }
}
