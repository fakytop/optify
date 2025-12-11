package com.optify.services;

import com.optify.domain.Store;
import com.optify.exceptions.DataException;
import com.optify.repository.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    // --------------------------------------------------------
    // MÉTODOS DE ESCRITURA: UPSERT
    // --------------------------------------------------------
    @Transactional
    public Store createOrUpdateStore(Store storeDetails) throws DataException {
        // (El cuerpo de este método se mantiene igual que el que me enviaste)
        Optional<Store> existingStoreOptional = storeRepository.findByName(storeDetails.getName());

        if (existingStoreOptional.isPresent()) {
            // ES UN REGISTRO EXISTENTE: HACEMOS UPDATE
            Store s = existingStoreOptional.get();

            // 1. Validamos si hay conflicto de RUT
            if (storeDetails.getRut() != 0 && s.getRut() != 0 && s.getRut() != storeDetails.getRut()) {
                throw new DataException("[DataException] Conflicto: La tienda ya tiene asignado otro RUT (" + s.getRut() + ").");
            }

            // 2. Si la tienda existente tiene RUT=0 (creada por scrapper), le permitimos actualizarlo.
            if (s.getRut() == 0 && storeDetails.getRut() != 0) {
                if (storeRepository.findByRut(storeDetails.getRut()).isPresent() &&
                        storeRepository.findByRut(storeDetails.getRut()).get().getId() != s.getId()) {
                    throw new DataException("[DataException] El RUT " + storeDetails.getRut() + " ya está asociado a otra tienda.");
                }
                s.setRut(storeDetails.getRut());
            }

            // 3. Actualizar otros campos
            s.setFantasyName(storeDetails.getFantasyName());
            s.setHomePage(storeDetails.getHomePage());

            return storeRepository.save(s);

        } else {
            // ES UN REGISTRO NUEVO: HACEMOS CREATE
            if(storeDetails.getRut() == 0) {
                throw new DataException("[DataException] El RUT es obligatorio para crear una tienda nueva.");
            }
            if(storeRepository.findByRut(storeDetails.getRut()).isPresent()) {
                throw new DataException("[DataException] Ya existe un supermercado con ese RUT: [" + storeDetails.getRut() + "]");
            }
            return storeRepository.save(storeDetails);
        }
    }

    // --------------------------------------------------------
    // MÉTODOS DE LECTURA (FALTANTES)
    // --------------------------------------------------------

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // MÉTODO FALTANTE 1: getStoreByRut (soluciona error de Facade)
    public Optional<Store> getStoreByRut(long rut) {
        return storeRepository.findByRut(rut);
    }

    // MÉTODO FALTANTE 2: addUrlCategoryToStore (soluciona error de Facade)
    @Transactional
    public void addUrlCategoryToStore(long rut, String category) throws DataException {
        try {
            Store store = storeRepository.findByRut(rut)
                    .orElseThrow(() -> new NoSuchElementException("No se encontró tienda con el RUT."));

            store.addUrlCategory(category);
            storeRepository.save(store);
        } catch (NoSuchElementException e) {
            throw new DataException("[DataException] No se encontró super con el RUT: ["
                    + rut+"]. \nMensaje Original: " + e.getMessage());
        }
    }
}