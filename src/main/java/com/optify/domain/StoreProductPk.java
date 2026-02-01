package com.optify.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StoreProductPk implements Serializable {
    private long storeRut;
    private int productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        StoreProductPk that = (StoreProductPk) o;
        return storeRut == that.storeRut && productId == that.productId;
    }

    @Override
    public int hashCode() {

        return  Objects.hash(storeRut, productId);
    }

    public long getStoreRut() {
        return storeRut;
    }

    public void setStoreRut(long storeRut) {
        this.storeRut = storeRut;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "StoreProductPk{" +
                "storeRut=" + storeRut +
                ", productId=" + productId +
                '}';
    }
}
