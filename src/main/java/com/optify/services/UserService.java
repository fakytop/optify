package com.optify.services;

import com.optify.domain.Store;
import com.optify.domain.User;
import com.optify.dto.UserPasswordUpdateDto;
import com.optify.dto.UserRegisterDto;
import com.optify.dto.UserLoginDto;
import com.optify.dto.UserUpdateDto;
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
    public User register(UserRegisterDto userRegisterDto) throws AuthenticationException, DataException {
        if(userRegisterDto.getUserCi() == 0) {
            throw new AuthenticationException("[Authentication] Debe ingresar una cédula de identidad válida.");
        }
        if(userRepository.findByUsername(userRegisterDto.getUserUsername()).isPresent()) {
            throw new AuthenticationException("[Authentication] Ya existe el nombre de usuario: " + userRegisterDto.getUserUsername());
        }
        if(userRepository.existsById(userRegisterDto.getUserCi())) {
            throw new AuthenticationException("[Authentication] La cédula de identidad ya está registrada.");
        }
        if(userRepository.findByMail(userRegisterDto.getUserMail()).isPresent()) {
            throw new AuthenticationException("[Authentication] Ya existe el e-mail: " + userRegisterDto.getUserMail());
        }
        User user = new User();
        Store preferredStore = storeService.getStoreByRut(userRegisterDto.getUserPreferredStore());
        user.setRegisterData(userRegisterDto,preferredStore);
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

    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfile(String username, UserUpdateDto userUpdateDto) throws DataException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataException("User not found."));

        if (userUpdateDto.getEmail() != null) {
            user.setMail(userUpdateDto.getEmail());
        }

        if (userUpdateDto.getUserPreferredDay() != null) {
            user.setPreferredDay(userUpdateDto.getUserPreferredDay());
        }

        if (userUpdateDto.getUserPreferredStore() != null) {
            Store preferredStore = storeService.getStoreByRut(userUpdateDto.getUserPreferredStore());
            user.setPreferredStore(preferredStore);
        }

        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeUserPassword(String username, UserPasswordUpdateDto userPasswordUpdateDto) throws AuthenticationException, DataException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataException("User not found."));

        if (!encoder.matches(userPasswordUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new AuthenticationException("Current password is incorrect.");
        }

        user.setPassword(encoder.encode(userPasswordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }
}
