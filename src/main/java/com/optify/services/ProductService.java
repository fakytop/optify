package com.optify.services;

import com.optify.domain.Product;
import com.optify.exceptions.DataException;
import com.optify.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) throws DataException {
        if(productRepository.findByEan(product.getEan()).isPresent()
                || productRepository.findByGtin(product.getGtin()).isPresent()) {
            throw new DataException(
                    "[StockException] Ya se encuentra el producto {ean: "
                            + product.getEan() + ", nombre: "
                            + product.getName() + "} guardado en la base de datos."
            );
        } else {
            return productRepository.save(product);
        }
    }

}
