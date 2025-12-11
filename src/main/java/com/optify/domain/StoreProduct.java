/*package com.optify.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
@Table(name = "store_products")
public class StoreProduct {
    //Clave compuesta
    @EmbeddedId
    private StoreProductPk id;

    //Relación con Store (Muchos StoreProduct a un Store)
    @ManyToOne
    @MapsId("storeRut") //Mapea la columna de storeRut de StoreProductPk
    @JoinColumn(name = "store_rut") //Columna FK en la BD
    private Store store;

    //Relación con Product (Muchos StoreProduct a un Product)
    @ManyToOne
    @MapsId("productEan") //Mapea la columna de productEan de StoreProductPk
    @JoinColumn(name = "product_ean") //Columna FK en la BD
    private Product product;

    private double price;
    private double lowPrice;
    private double highPrice;
    private Timestamp priceValidUntil;
    private String urlProduct;

    public StoreProduct() {
        this.id = new StoreProductPk();
    }

    public long getStoreRut() {
        return this.id.getStoreRut();
    }

    public void setStoreRut(long storeRut) {
        this.id.setStoreRut(storeRut);
    }

    public String getProductEan() {
        return this.id.getProductEan();
    }

    public void setProductEan(String productEan) {
        this.id.setProductEan(productEan);
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public void setUrlProduct(String urlProduct) {
        this.urlProduct = urlProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public Timestamp getPriceValidUntil() {
        return priceValidUntil;
    }

    public void setPriceValidUntil(Timestamp priceValidUntil) {
        this.priceValidUntil = priceValidUntil;
    }
}

 */package com.optify.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_products") // Usamos el nombre que indicaste
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con Product
    @ManyToOne
    @JoinColumn(name = "product_ean", nullable = false)
    private Product product;

    // Relación ManyToOne con Store
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // Datos de la captura de precio
    private BigDecimal price;
    private String currency;
    private LocalDateTime scrapeDate = LocalDateTime.now();

    // Constructores
    public StoreProduct() {}

    public StoreProduct(Product product, Store store, BigDecimal price, String currency) {
        this.product = product;
        this.store = store;
        this.price = price;
        this.currency = currency;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getScrapeDate() { return scrapeDate; }
    public void setScrapeDate(LocalDateTime scrapeDate) { this.scrapeDate = scrapeDate; }
}