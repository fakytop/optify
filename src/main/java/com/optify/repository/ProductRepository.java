package com.optify.repository;

import com.optify.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByEan(String ean);
    Optional<Product> findByGtin(String gtin);
    Optional<Product> findByName(String name);
    Page<Product> findByCategoryId(int categoryId, Pageable pageable);
}
