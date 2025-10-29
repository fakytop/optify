package com.optify.systems;

import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;

import java.util.HashMap;

public class UsersSystem {
    private static UsersSystem instance = new UsersSystem();

    private HashMap<String, User> users = new HashMap<>();

    public static UsersSystem getInstance() {
        return instance;
    }

    private UsersSystem() {

    }
    public void signIn(User user) throws AuthenticationException {
        if(users.containsKey(user.getUserName())) {
            throw new AuthenticationException("[Authentication] Ya existe el nombre de usuario: " + user.getUserName());
        }

        if(user.getCi() == 0) {
            throw new AuthenticationException("[Authentication] Debe ingresar una cédula de identidad válida.");
        }

        if(user.geteMail() == null || "".equalsIgnoreCase(user.geteMail())) {
            throw new AuthenticationException("[Authentication] Debe ingresar un e-mail válido.");
        }

        if(user.getName() == null || user.getLastName() == null || "".equalsIgnoreCase(user.getName()) || "".equalsIgnoreCase(user.getLastName())) {
            throw new AuthenticationException("[Authentication] Nombre y apellido no pueden ser vacíos.");
        }

        if(user.getCity() == null) {
            //throw new AuthenticationException("[Authentication] Debe seleccionar una ciudad.");
        }

        if(!validPassword(user.getPassword())) {
            throw new AuthenticationException("[Authentication] La contraseña debe cumplir con los requisitos mínimos.");
        }
        users.put(user.getUserName(),user);
    }

    public User logIn(String userName, String password) throws AuthenticationException {
        if(!users.containsKey(userName)) {
            throw new AuthenticationException("[Authentication] No existe el usuario.");
        }
        User user = users.get(userName);
        if(!user.getPassword().equals(password)) {
            throw new AuthenticationException("[Authentication] Clave de usuario incorrecta.");
        }
        return user;
    }

    private boolean validPassword(String password) {
        return true;
    }
}
