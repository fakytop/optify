package com.optify.repository;

import com.optify.domain.Product;
import org.springframework.data.domain.Page;     // 🚨 Importar Page
import org.springframework.data.domain.Pageable; // 🚨 Importar Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Optional<Product> findByEan(String ean);
    Optional<Product> findByGtin(String gtin);
    Optional<Product> findByName(String name); // Ya existe

    // 🚨 Nuevos métodos para soportar la búsqueda paginada en el Servicio
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByBrandContainingIgnoreCase(String brand, Pageable pageable);
    // Podrías añadir findByNameContainingIgnoreCaseAndBrandContainingIgnoreCase, etc.
}