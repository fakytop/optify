package com.optify.repository;

import com.optify.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {
    Optional<Product> findById(int id);
    Optional<Product> findByName(String name);
    Page<Product> findByCategoryId(int categoryId, Pageable pageable);
    @Query(value = "SELECT * FROM Products p " +
            "WHERE p.name % :term " +
            "ORDER BY similarity(p.name, :term) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Product> findSimilarByName(@Param("term") String term, @Param("limit") int limit);
}
