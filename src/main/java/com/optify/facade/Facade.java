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
    @Aut
    private Facade() {}

    // Métodos referidos al usuario.
    public User signIn(User user) throws AuthenticationException {
        return userService.signIn(user);
    }

    public User logIn(String username, String password) throws AuthenticationException {
        return userService.logIn(username, password);
    }

    //Métodos referidos al supermercado
    public Store addStore(Store store) throws DataException {
        return storeService.addStore(store);
    }

    public Store updateStore(Store store) throws DataException {
        return storeService.updateStore(store);
    }

    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    public Store getStoreByRut(long rut) {
        return storeService.getStoreByRut(rut).get();
    }

    public void addUrlCategoryByRut(long rut, String category) throws DataException {
        storeService.addUrlCategoryToStore(rut,category);
    }
}
