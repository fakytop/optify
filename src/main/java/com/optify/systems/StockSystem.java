package com.optify.systems;

import com.optify.domain.Product;
import com.optify.domain.StoreProduct;

import java.util.ArrayList;

public class StockSystem {
    private static StockSystem instance = new StockSystem();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<StoreProduct> storeProducts = new ArrayList<>();

    public StockSystem getInstance() {return instance;}

    public StockSystem() {

    }

    public Product existProduct(Product product) {

//          todito deberia ser un select a la base de datos, si existe me lo quedo, sino retorno null
        for(Product p : products) {
            if(p.getEan().equals(product.getEan())) {
                return p;
            }
            if(p.getGtin().equals(product.getGtin())) {
                return p;
            }
        }
        return null;
    }

    public Product addProduct(Product product) {
        Product p = existProduct(product);
        if(p != null) {
            return p;
        }
        products.add(product);
        return product;
    }

    public void addOrUpdateProductInfo(StoreProduct storeProduct) {

    }

}
