package com.optify.services;

import com.optify.domain.Store;
import com.optify.dto.StoreAddDto;
import com.optify.exceptions.DataException;
import com.optify.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Transactional(rollbackFor = Exception.class)
    public Store addStore(Store store) throws DataException {
        if(storeRepository.findByRut(store.getRut()).isPresent()) {
            throw new DataException("[DataException] Ya existe supermercado con ese rut: [" + store.getRut() + "]");
        }
        return storeRepository.save(store);
    }

    @Transactional(rollbackFor = Exception.class)
    public Store updateStore(Store store) throws DataException {
        if(storeRepository.findByRut(store.getRut()).isPresent()) {
            Store s = storeRepository.findByRut(store.getRut()).get();
            if(store.getName() != null && store.getName() != "") {
                s.setName(store.getName());
            }
            if(store.getFantasyName() != null && store.getFantasyName() != "") {
                s.setFantasyName(store.getFantasyName());
            }
            if(store.getHomePage() != null && store.getHomePage() != "") {
                s.setHomePage(store.getHomePage());
            }
            return storeRepository.save(s);
        }
        throw new DataException("[DataException] No se encontró supermercado con rut: [" + store.getRut() + "]");
    }

    public void deleteStore(long rut) throws DataException {
        if(!storeRepository.findByRut(rut).isPresent()) {
            throw new DataException("[DataException] No se encontró supermercado con rut: {" + rut + "}");
        }
        storeRepository.delete(storeRepository.findByRut(rut).get());
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreByRut(long rut) throws DataException {
        if(!storeRepository.findByRut(rut).isPresent()) {
            throw new DataException("[DataException] No se encontró supermercado con RUT: {" + rut + "}");
        }
        return storeRepository.findByRut(rut).get();
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
