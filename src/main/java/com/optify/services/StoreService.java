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
    public Store updateStore(Store updateStore) throws DataException {
        Optional<Store> optionalStore = storeRepository.findByRut(updateStore.getRut());
        if(optionalStore.isPresent()) {
            Store store = optionalStore.get();
            if(updateStore.getName() != null && updateStore.getName() != "") {
                store.setName(updateStore.getName());
            }
            if(updateStore.getFantasyName() != null && updateStore.getFantasyName() != "") {
                store.setFantasyName(updateStore.getFantasyName());
            }
            if(updateStore.getHomePage() != null && updateStore.getHomePage() != "") {
                store.setHomePage(updateStore.getHomePage());
            }
            return storeRepository.save(store);
        }
        throw new DataException("[DataException] No se encontró supermercado con rut: [" + updateStore.getRut() + "]");
    }

    public void deleteStore(long rut) throws DataException {
        Optional<Store> optionalStore = storeRepository.findByRut(rut);
        if(!optionalStore.isPresent()) {
            throw new DataException("[DataException] No se encontró supermercado con rut: {" + rut + "}");
        }
        storeRepository.delete(optionalStore.get());
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreByRut(long rut) throws DataException {
        Optional<Store> optionalStore = storeRepository.findByRut(rut);
        if(!optionalStore.isPresent()) {
            throw new DataException("[DataException] No se encontró supermercado con RUT: {" + rut + "}");
        }
        return optionalStore.get();
    }
}
