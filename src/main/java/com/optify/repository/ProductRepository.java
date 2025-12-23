package com.optify.repository;

import com.optify.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Optional<Product> findByEan(String ean);
    Optional<Product> findByGtin(String gtin);
    Optional<Product> findByName(String name);
    List<Product> findByCategoryId(int categoryId);
}
