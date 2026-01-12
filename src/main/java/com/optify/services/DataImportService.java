package com.optify.services;

import com.optify.domain.Category;
import com.optify.domain.Product;
import com.optify.domain.Store;
import com.optify.domain.StoreProduct;
import com.optify.dto.ProductDto;
import com.optify.exceptions.DataException;
import com.optify.utils.ComparisonUtils;
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
        if(dto.getProductEan() != null) {
            product = productService.getProductByEan(dto.getProductEan());
        }
        if(product == null) {
            product = findProductBySimilarName(dto.getProductName());
        }

        if(product == null) {
            product = new Product();
            setProductData(product, dto);
            Category category = null;
            String normalizedDiacriticalMarksName = ComparisonUtils.deleteDiacriticalMarks(dto.getCategoryName());
            category = categoryService.getCategoryByName(normalizedDiacriticalMarksName);
            if(category == null) {
                category = new Category();
                category.setName(normalizedDiacriticalMarksName);
                category.setDescription(dto.getCategoryDescription());
                category = categoryService.addCategory(category);
            }
            product.setCategory(category);
            productService.addProduct(product);
        }

        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setProduct(product);
        Store store = storeService.getStoreByRut(dto.getStoreRut());
        storeProduct.setStore(store);
        storeProduct.setUrlProduct(dto.getUrlProduct());
        storeProduct.setPrice(dto.getProductPrice());
        storeProductService.addOrUpdateStoreProduct(storeProduct);
    }

    private Product findProductBySimilarName(String productName) throws DataException {
        List<Product> productsCandidates = productService.getSimilarCandidates(productName);
        if(productsCandidates.isEmpty()) {
            return null;
        }
        for(Product product : productsCandidates){
            boolean isSameProduct = ComparisonUtils.compare(productName,product.getName(),true);
            if(isSameProduct) {
                return product;
            }
        }
        return null;
    }

    private void setProductData(Product product, ProductDto dto) {
        product.setEan(dto.getProductEan());
        String utfName = ComparisonUtils.repairEncoding(dto.getProductName());
        product.setName(utfName);
        product.setGtin(dto.getProductGtin());
        product.setDescription(dto.getProductDescription());
        product.setImageUrl(dto.getProductImageUrl());
        product.setBrand(dto.getProductBrand());
    }

}
