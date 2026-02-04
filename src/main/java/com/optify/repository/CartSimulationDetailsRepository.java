package com.optify.repository;

import com.optify.domain.CartSimulationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartSimulationDetailsRepository extends JpaRepository<CartSimulationDetail,Integer> {
    List<CartSimulationDetail> findByProductId(int productId);
}
