package com.optify.services;

import com.optify.domain.Store;
import com.optify.domain.User;
import com.optify.dto.UserDto;
import com.optify.dto.UserLoginDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.repository.UserRepository;
import com.optify.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreService storeService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional(rollbackFor = Exception.class)
    public User register(UserDto userDto) throws AuthenticationException, DataException {
        if(userDto.getUserCi() == 0) {
            throw new AuthenticationException("[Authentication] Debe ingresar una cédula de identidad válida.");
        }
        if(userRepository.findByUsername(userDto.getUserUsername()).isPresent()) {
            throw new AuthenticationException("[Authentication] Ya existe el nombre de usuario: " + userDto.getUserUsername());
        }
        if(userRepository.existsById(userDto.getUserCi())) {
            throw new AuthenticationException("[Authentication] La cédula de identidad ya está registrada.");
        }
        if(userRepository.findByMail(userDto.getUserMail()).isPresent()) {
            throw new AuthenticationException("[Authentication] Ya existe el e-mail: " + userDto.getUserMail());
        }
        User user = new User();
        Store preferredStore = storeService.getStoreByRut(userDto.getUserPreferredStore());
        user.setRegisterData(userDto,preferredStore);
        String passwordHash = encoder.encode(user.getPassword());
        user.setPassword(passwordHash);

        return userRepository.save(user);
    }

    public String logIn(UserLoginDto userDto) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUserUsername());

        if(optionalUser.isEmpty()) {
            throw new AuthenticationException("[Authentication] No existe el nombre de usuario: " + userDto.getUserUsername());
        }
        User user = optionalUser.get();

        if(!encoder.matches(userDto.getUserPassword(),user.getPassword())) {
            throw new AuthenticationException("[Authentication] Clave de usuario incorrecta.");
        }
        return jwtUtil.generateToken(user.getUsername());
    }
}
