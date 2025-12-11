package com.optify.services;

import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;
import com.optify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User signIn(User user) throws AuthenticationException {
        if(user.getCi() == 0) {
            throw new AuthenticationException("[Authentication] Debe ingresar una cédula de identidad válida.");
        }
        if(userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new AuthenticationException("[Authentication] Ya existe el nombre de usuario: " + user.getUserName());
        }
        if(userRepository.existsById(user.getCi())) {
            throw new AuthenticationException("[Authentication] La cédula de identidad ya está registrada.");
        }
        // CORREGIDO: Ahora usa findByMail (esperando que Repository coincida)
        if(userRepository.findByMail(user.getMail()).isPresent()) {
            throw new AuthenticationException("[Authentication] Ya existe el e-mail: " + user.getMail());
        }
        user.validPassword();

        String passwordHash = encoder.encode(user.getPassword());
        user.setPassword(passwordHash);

        return userRepository.save(user);
    }

    public User logIn(String userName, String password) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        if(optionalUser.isEmpty()) {
            throw new AuthenticationException("[Authentication] No existe el nombre de usuario: " + userName);
        }
        User user = optionalUser.get();

        if(!encoder.matches(password,user.getPassword())) {
            throw new AuthenticationException("[Authentication] Clave de usuario incorrecta.");
        }
        return user;
    }
}