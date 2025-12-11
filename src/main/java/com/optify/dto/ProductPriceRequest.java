package com.optify.dto;

import java.math.BigDecimal;

public class ProductPriceRequest {

    // Campos de Producto
    private String name;
    private String brand;
    private String sku;
    private String gtin; // El identificador único para el Upsert de Producto
    private String descripcion;
    private String imageUrl; // Antes: url_imagen_principal

    // Campos de Tienda/Precio
    private String store; // Antes: supermercado
    private String categoria;
    private String precio;
    private String moneda;

    // Otros campos
    private Integer id_scrape;
    private String url_producto;

    // --- Getters y Setters ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getGtin() { return gtin; }
    public void setGtin(String gtin) { this.gtin = gtin; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public Integer getId_scrape() { return id_scrape; }
    public void setId_scrape(Integer id_scrape) { this.id_scrape = id_scrape; }

    public String getUrl_producto() { return url_producto; }
    public void setUrl_producto(String url_producto) { this.url_producto = url_producto; }
}