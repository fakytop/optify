package com.optify.services;

import com.optify.domain.Product;
import com.optify.domain.Store;
import com.optify.domain.StoreProduct;
import com.optify.exceptions.DataException;
import com.optify.repository.ProductRepository;
import com.optify.specifications.ProductSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StoreProductService storeProductService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product addProduct(Product product) throws DataException {
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

    public Product getProductByName(String name)  {
        Optional<Product> optionalProduct = productRepository.findByName(name);
        if(!optionalProduct.isPresent()) {
            return null;
        }
        return optionalProduct.get();
    }

    public Page<Product> searchProductsByName(String term, Pageable pageable) throws DataException {
        Specification<Product> spec = ProductSpecifications.searchByNameMultiWord(term);
        Page<Product> products = productRepository.findAll(spec,pageable);
        if(products.isEmpty()) {
            throw new DataException("[SEARCH] No se encontraron productos con la búsqueda especificada.");
        }
        return products;
    }

    public Product getProductById(int id) throws DataException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(!optionalProduct.isPresent()) {
            throw new DataException("No se encuentra el producto id: {" + id + "}");
        }
        return optionalProduct.get();
    }

    public List<Product> getSimilarCandidates(String name,long storeRut,long idWeb) {
        return productRepository.findSimilarByName(name, storeRut, idWeb,10);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public void flush() {
        productRepository.flush();
    }
}
