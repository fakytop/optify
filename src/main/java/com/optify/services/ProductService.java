package com.optify.services;

import com.optify.domain.Product;
import com.optify.domain.Store;
import com.optify.domain.StoreProduct;
import com.optify.dto.ProductPriceRequest;
import com.optify.exceptions.DataException;
import com.optify.repository.ProductRepository;
import com.optify.repository.StoreRepository;
import com.optify.repository.StoreProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private StoreProductRepository storeProductRepository;

    /**
     * NUEVO METODO: Devuelve todos los productos desde la base de datos.
     */
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }


    @Transactional
    public StoreProduct processPriceCapture(ProductPriceRequest capture) throws DataException {

        // 1. VALIDACIÓN BÁSICA
        if (capture.getGtin() == null || capture.getGtin().isEmpty()) {
            throw new DataException("GTIN es obligatorio para identificar o crear el producto.");
        }

        // 2. OBTENER O CREAR PRODUCTO (UPSERT)
        Product product = productRepository.findByGtin(capture.getGtin())
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setEan(capture.getGtin());
                    newProduct.setGtin(capture.getGtin());
                    newProduct.setSku(capture.getSku());
                    newProduct.setName(capture.getName());
                    newProduct.setDescription(capture.getDescripcion());
                    newProduct.setBrand(capture.getBrand());
                    newProduct.setImageUrl(capture.getImageUrl());

                    return productRepository.save(newProduct);
                });

        // 3. OBTENER O CREAR TIENDA (UPSERT)
        Store store = storeRepository.findByName(capture.getStore())
                .orElseGet(() -> {
                    Store newStore = new Store(capture.getStore());
                    return storeRepository.save(newStore);
                });

        // 4. CREAR REGISTRO DE PRECIO
        try {
            String rawPrice = capture.getPrecio().replace(",", "").replace("$", "").trim();
            BigDecimal price = new BigDecimal(rawPrice);

            StoreProduct priceEntry = new StoreProduct(
                    product,
                    store,
                    price,
                    capture.getMoneda()
            );
            return storeProductRepository.save(priceEntry);

        } catch (NumberFormatException e) {
            throw new DataException("Error al procesar el precio ('" + capture.getPrecio() + "'). Debe ser un formato numérico válido.");
        }
    }
}