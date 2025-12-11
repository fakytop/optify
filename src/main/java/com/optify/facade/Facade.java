package com.optify.facade;

import com.optify.domain.Store;
import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.services.StoreService;
import com.optify.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class Facade {

    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;

    private Facade() {}

    // --- Métodos referidos al usuario ---
    // CORRECCIÓN 1: signIn debe lanzar DataException (como lo usa ConsoleView)
    public User signIn(User user) throws AuthenticationException, DataException {
        return userService.signIn(user);
    }

    public User logIn(String username, String password) throws AuthenticationException {
        return userService.logIn(username, password);
    }

    // --- Métodos referidos al supermercado (CORREGIDOS) ---
    public Store addStore(Store store) throws DataException {
        return storeService.createOrUpdateStore(store);
    }

    public Store updateStore(Store store) throws DataException {
        return storeService.createOrUpdateStore(store);
    }

    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    public Optional<Store> getStoreByRut(long rut) {
        return storeService.getStoreByRut(rut);
    }

    // CORRECCIÓN FINAL: Se elimina el 'return' de un método void
    public void addUrlCategoryByRut(long rut, String category) throws DataException {
        storeService.addUrlCategoryToStore(rut, category); // ¡SIN 'return'!
    }
}