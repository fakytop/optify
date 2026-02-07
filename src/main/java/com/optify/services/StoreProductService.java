package com.optify.services;

import com.optify.domain.StoreProduct;
import com.optify.domain.StoreProductPk;
import com.optify.exceptions.DataException;
import com.optify.repository.StoreProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StoreProductService {
    @Autowired
    private StoreProductRepository storeProductRepository;
    @Autowired
    private DiscardReferenceService discardReferenceService;

    public StoreProduct getStoreProductById(StoreProductPk id) {
        Optional<StoreProduct> optionalStoreProduct = storeProductRepository.findById(id);
        if(!optionalStoreProduct.isPresent()) {
            return null;
        }
        return optionalStoreProduct.get();
    }

    public StoreProduct addOrUpdateStoreProduct(StoreProduct storeProduct) {
        return storeProductRepository.save(storeProduct);
    }

    public void deleteStoreProduct(StoreProduct storeProduct) {
        storeProductRepository.delete(storeProduct);
    }

    public int getIdProduct(long idWeb, long rut) {
        if(storeProductRepository.findByIdWebAndStore_Rut(idWeb, rut).isPresent()) {
            StoreProduct storeProduct = storeProductRepository.findByIdWebAndStore_Rut(idWeb,rut).get();
            return storeProduct.getId().getProductId();
        }
        return -1;
    }

    public List<StoreProduct> getStoreProductsByProductIds(List<Integer> productIds) {
        return storeProductRepository.findByProduct_IdInOrderByProduct_IdAsc(productIds);
    }

    public List<StoreProduct> getStoreProductsByProductId(int productIds){
        return storeProductRepository.findByProduct_id(productIds);
    }

    public String getFirstUrlByProductId(long productId) throws DataException {
        List<StoreProduct> storeProduct = storeProductRepository.findByProduct_id(productId);
        if(storeProduct.isEmpty()) {
           throw new DataException("No se encontraron productos con el código {" + productId + "}");
        }
        return storeProduct.get(0).getUrlProduct();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStoreProductWithId(StoreProductPk spId) throws DataException {
        StoreProduct deleteStoreProduct = getStoreProductById(spId);
        if(deleteStoreProduct == null) {
            throw new DataException("No se encontró el producto con id: {" + spId.getProductId() + "} y RUT: " + spId.getStoreRut() + "}");
        }
        discardReferenceService.addDiscardedReference(deleteStoreProduct.getUrlProduct());
        deleteStoreProduct(deleteStoreProduct);
    }
}
