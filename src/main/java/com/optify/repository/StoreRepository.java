package com.optify.repository;

import com.optify.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // Necesario para la lógica de captura de precios (buscar por nombre)
    Optional<Store> findByName(String name);

    // Necesario para la lógica de consola/servicios antiguos (buscar por rut)
    Optional<Store> findByRut(long rut);
}