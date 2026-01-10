package com.optify.services;

import com.optify.domain.Product;
import com.optify.exceptions.DataException;
import com.optify.repository.ProductRepository;
import com.optify.specifications.ProductSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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
        }
        return productRepository.save(product);

    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByCategoryId(int categoryId, Pageable pageable) throws DataException {
        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable);
        if(products.isEmpty()) {
            throw new DataException("[DataException] No se encontraron productos para la categoría {" + categoryId + "}");
        }
        return products;
    }

    public Product getProductByEan(String ean) {
        if(!productRepository.findByEan(ean).isPresent()) {
            return null;
        }
        return productRepository.findByEan(ean).get();
    }

    public Product getProductByName(String name)  {
        if(!productRepository.findByName(name).isPresent()) {
            return null;
        }
        return productRepository.findByName(name).get();
    }

    public Page<Product> searchProductsByName(String term, Pageable pageable) throws DataException {
        Specification<Product> spec = ProductSpecifications.searchByNameMultiWord(term);
        Page<Product> products = productRepository.findAll(spec,pageable);
        if(products.isEmpty()) {
            throw new DataException("[SEARCH] No se encontraron productos con la búsqueda especificada.");
        }
        return products;
    }
}
