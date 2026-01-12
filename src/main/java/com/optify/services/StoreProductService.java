package com.optify.services;

import com.optify.domain.StoreProduct;
import com.optify.domain.StoreProductPk;
import com.optify.exceptions.DataException;
import com.optify.repository.StoreProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreProductService {
    @Autowired
    private StoreProductRepository storeProductRepository;

    public StoreProduct findById(StoreProductPk id) throws DataException {
        if(!storeProductRepository.findById(id).isPresent()) {
            throw new DataException("[DataException] No se encontró el producto { ean: " + id.getProductId() + "; rut: " + id.getStoreRut() + "}");
        }
        return storeProductRepository.findById(id).get();
    }

    public StoreProduct addOrUpdateStoreProduct(StoreProduct storeProduct) {
        return storeProductRepository.save(storeProduct);
    }

}
