package com.optify.systems;

import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;

public class UsersSystem {
    private static UsersSystem instance = new UsersSystem();
    private HashMap<String, User> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        user.validPassword(user.getPassword());
        //hasheo la contraseña para seguridad.
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        users.put(user.getUserName(),user);
    }

    public User logIn(String userName, String password) throws AuthenticationException {
        if(!users.containsKey(userName)) {
            throw new AuthenticationException("[Authentication] No existe el usuario.");
        }
        User user = users.get(userName);
        if(!passwordEncoder.matches(password,user.getPassword())) {
            throw new AuthenticationException("[Authentication] Clave de usuario incorrecta.");
        }
        return user;
    }


}
