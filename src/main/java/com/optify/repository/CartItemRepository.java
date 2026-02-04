package com.optify.repository;

import com.optify.domain.CartItem;
import com.optify.domain.CartItemPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemPk> {
    List<CartItem> findByProductId(long productId);
    Optional<CartItem> findById(CartItemPk cartItemPk);
}
