package com.optify.services;

import com.optify.domain.*;
import com.optify.dto.ProductImportDto;
import com.optify.exceptions.DataException;
import com.optify.repository.ManualMatchRepository;
import com.optify.utils.ComparisonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    @Autowired
    private ManualMatchService manualMatchService;


    @Transactional(rollbackFor = Exception.class) //Si algo falla, vuelve atrás.
    public void importProductsBatch(List<ProductImportDto> dtos) throws DataException {
        for(ProductImportDto dto : dtos){
            this.importProductFromStoreData(dto);
        }
    }

    private Product findProductFromStoreProductToUpdate(ProductImportDto dto,Store store) throws DataException {
        if(dto.getIdWeb() != 0 && store != null) {
            int id = storeProductService.getIdProduct(dto.getIdWeb(),dto.getStoreRut());
            if(id != -1){
                return productService.getProductById(id);
            }
        }
        return null;
    }


    public void importProductFromStoreData(ProductImportDto dto) throws DataException {
        if(dto.getIdWeb() == 0) {
            throw new DataException("El código idWeb no puede ser 0.");
        }
        Store store = storeService.getStoreByRut(dto.getStoreRut());
        Product product = findProductFromStoreProductToUpdate(dto,store);
        HashMap<String,Product> productResult = null;
        if(product == null) {
            productResult = findProductBySimilarName(dto);
        }
        if(product == null && productResult == null) {
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
        if(product == null && productResult != null && productResult.containsKey("final")) {
            product = productResult.get("final");
        } else if(product == null && productResult != null && productResult.containsKey("control")) {
            if(!manualMatchService.existsByStoreAndIdWeb(store,dto.getIdWeb())) {
                String urlProductDB = storeProductService.getFirstUrlByProductId(productResult.get("control").getId());
                ManualMatchPending pending = new ManualMatchPending(productResult.get("control"),store,dto.getIdWeb(),
                        dto.getProductName(),dto.getProductDescription(),dto.getProductImageUrl(),dto.getProductBrand(),
                        dto.getCategoryName(),dto.getUrlProduct(),dto.getProductPrice(),urlProductDB
                );
                manualMatchService.addManualMatch(pending);
            }
        }

        if(product != null) {
            saveStoreProduct(product,store,dto);
        }

    }

    public void saveStoreProduct(Product product, Store store, ProductImportDto dto) {
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setProduct(product);
        storeProduct.setStore(store);
        storeProduct.setIdWeb(dto.getIdWeb());
        storeProduct.setUrlProduct(dto.getUrlProduct());
        storeProduct.setPrice(dto.getProductPrice());
        storeProductService.addOrUpdateStoreProduct(storeProduct);
    }

    private HashMap<String,Product> findProductBySimilarName(ProductImportDto productImportDto) throws DataException {
        String productName = productImportDto.getProductName();
        String brandName = productImportDto.getProductBrand() != null ? productImportDto.getProductBrand().toLowerCase().trim() : null;
        long storeRut = productImportDto.getStoreRut();
        long idWeb = productImportDto.getIdWeb();
        List<Product> productsCandidates = productService.getSimilarCandidates(productName,storeRut,idWeb);
        if(productsCandidates.isEmpty()) {
            return null;
        }
        for(Product product : productsCandidates){
/*          String candidateBrand = product.getBrand() != null ? product.getBrand().toLowerCase().trim() : null;
            if(candidateBrand != null && brandName != null && !candidateBrand.equals(brandName)) {
                continue;
            }
*/
            HashMap<String,Boolean> isSameProduct = ComparisonUtils.compare(productName,product.getName());
            HashMap<String,Product> productResult = new HashMap<>();
            if(isSameProduct.get("finalResult") && !isSameProduct.get("doControl")) {
                productResult.put("final",product);
                return productResult;
            } else if(!isSameProduct.get("finalResult") && isSameProduct.get("doControl")) {
                productResult.put("control",product);
                return productResult;
            }
        }
        return null;
    }

    private void setProductData(Product product, ProductImportDto dto) {
        String utfName = ComparisonUtils.repairEncoding(dto.getProductName());
        product.setName(utfName);
        product.setDescription(dto.getProductDescription());
        product.setImageUrl(dto.getProductImageUrl());
        product.setBrand(dto.getProductBrand());
    }

}
