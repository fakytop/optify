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

    public Store addStore(Store store) throws DataException {
        if(storeRepository.findByRut(store.getRut()).isPresent()) {
            throw new DataException("[DataException] Ya existe supermercado con ese rut: [" + store.getRut() + "]");
        }
        return storeRepository.save(store);
    }

    public Store updateStore(Store store) throws DataException {
        if(storeRepository.findByRut(store.getRut()).isPresent()) {
            Store s = storeRepository.findByRut(store.getRut()).get();
            s.setName(store.getName());
            s.setHomePage(store.getHomePage());
            s.setFantasyName(store.getFantasyName());
            return storeRepository.save(s);
        }
        throw new DataException("[DataException] No se encontró supermercado con rut: [" + store.getRut() + "]");
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreByRut(long rut) {
        return storeRepository.findByRut(rut);
    }

    @Transactional
    public void addUrlCategoryToStore(long rut, String category) throws DataException {
        try {
            Store store = storeRepository.findByRut(rut).get();
            store.addUrlCategory(category);
            storeRepository.save(store);
        } catch (NoSuchElementException e) {
            throw new DataException("[DataException] No se encontró super con el RUT: ["
                    + rut+"]. \nMensaje Original: " + e.getMessage());
        }
    }
}
