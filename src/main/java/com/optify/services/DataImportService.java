package com.optify.services;

import com.optify.domain.Category;
import com.optify.domain.Product;
import com.optify.domain.Store;
import com.optify.domain.StoreProduct;
import com.optify.dto.ProductDto;
import com.optify.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataImportService {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private StoreService storeService;


    @Transactional(rollbackFor = Exception.class) //Si algo falla, vuelve atrás.
    public void importProductsBatch(List<ProductDto> dtos) throws DataException {
        for(ProductDto dto : dtos){
            this.importProductFromStoreData(dto);
        }
    }


    public void importProductFromStoreData(ProductDto dto) throws DataException {
        Product product = null;
        product = productService.getProductByEan(dto.getProductEan());
        if(product == null) {
            product = productService.getProductByName(dto.getProductName());
        }

        if(product == null) {
            product = new Product();
            setProductData(product, dto);
            Category category = null;
            category = categoryService.getCategoryByName(dto.getCategoryName());
            if(category == null) {
                category = new Category();
                category.setName(dto.getCategoryName());
                category.setDescription(dto.getCategoryDescription());
                category = categoryService.addCategory(category);
            }
            product.setCategory(category);
            productService.addProduct(product);
        }

        StoreProduct storeProduct = new StoreProduct();
        //TODO: Puede ser null xq lo estoy seteando yo al producto con lo que viene del scrapper,
        // hay que controlar aparte que la futura PK del producto no sea nula (no puede ser solo EAN).
        storeProduct.setProduct(product);
        Store store = storeService.getStoreByRut(dto.getStoreRut());
        storeProduct.setStore(store);
        storeProduct.setUrlProduct(dto.getUrlProduct());
        storeProduct.setPrice(dto.getProductPrice());
        storeProductService.addOrUpdateStoreProduct(storeProduct);
    }

    private void setProductData(Product product, ProductDto dto) {
        product.setEan(dto.getProductEan());
        product.setName(dto.getProductName());
        product.setGtin(dto.getProductGtin());
        product.setDescription(dto.getProductDescription());
        product.setImageUrl(dto.getProductImageUrl());
        product.setBrand(dto.getProductBrand());
    }

}
