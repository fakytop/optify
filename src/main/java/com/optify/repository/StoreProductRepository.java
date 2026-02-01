package com.optify.repository;

import com.optify.domain.StoreProduct;
import com.optify.domain.StoreProductPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, StoreProductPk> {
    Optional<StoreProduct> findById(StoreProductPk id);
    Optional<StoreProduct> findByIdWebAndStore_Rut(long idWeb, long rut);
    List<StoreProduct> findByProduct_IdInOrderByProduct_IdAsc(List<Integer> productIds);
}
