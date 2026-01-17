package com.optify.services;

import com.optify.domain.*;
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
        Store store = storeService.getStoreByRut(dto.getStoreRut());
        Product product = null;
        if(dto.getIdWeb() != 0 && store != null) {
            int id = storeProductService.getIdProduct(dto.getIdWeb(),dto.getStoreRut());
            if(id != -1){
                product = productService.getProductById(id);
            }
        }
        if(product == null) {
            product = findProductBySimilarName(dto);
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
                category = categoryService.addCategory(category);
            }
            product.setCategory(category);
            productService.addProduct(product);
        }

        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setProduct(product);
        storeProduct.setStore(store);
        storeProduct.setIdWeb(dto.getIdWeb());
        storeProduct.setUrlProduct(dto.getUrlProduct());
        storeProduct.setPrice(dto.getProductPrice());
        storeProductService.addOrUpdateStoreProduct(storeProduct);
    }

    private Product findProductBySimilarName(ProductDto productDto) throws DataException {
        String productName = productDto.getProductName();
        String brandName = productDto.getProductBrand() != null ? productDto.getProductBrand().toLowerCase().trim() : null;

        List<Product> productsCandidates = productService.getSimilarCandidates(productName);
        if(productsCandidates.isEmpty()) {
            return null;
        }
        for(Product product : productsCandidates){
            String candidateBrand = product.getBrand() != null ? product.getBrand().toLowerCase().trim() : null;
            if(candidateBrand != null && brandName != null &&  !candidateBrand.equals(brandName)) {
                continue;
            }

            boolean isSameProduct = ComparisonUtils.compare(productName,product.getName());
            if(isSameProduct) {
                return product;
            }
        }
        return null;
    }

    private void setProductData(Product product, ProductDto dto) {
        String utfName = ComparisonUtils.repairEncoding(dto.getProductName());
        product.setName(utfName);
        product.setDescription(dto.getProductDescription());
        product.setImageUrl(dto.getProductImageUrl());
        product.setBrand(dto.getProductBrand());
    }

}
