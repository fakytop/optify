package com.optify.api;

import com.optify.domain.User;
import com.optify.dto.LoginRequest;
import com.optify.dto.RegisterRequest;
import com.optify.exceptions.AuthenticationException;
import com.optify.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /users/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Mapeo de DTO a Entidad de Dominio
            User userToRegister = new User();
            userToRegister.setCi(registerRequest.getCi());
            userToRegister.setName(registerRequest.getName());
            userToRegister.setLastName(registerRequest.getLastName()); // CORREGIDO
            userToRegister.setUserName(registerRequest.getUserName());
            userToRegister.setMail(registerRequest.getMail()); // CORREGIDO
            userToRegister.setPassword(registerRequest.getPassword());

            User registeredUser = userService.signIn(userToRegister);

            // Ocultar la contraseña hasheada
            registeredUser.setPassword(null);

            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            // Status 400 (Bad Request)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // POST /users/login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User authenticatedUser = userService.logIn(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            );

            // Ocultamos la contraseña antes de responder
            authenticatedUser.setPassword(null);

            // Status 200 (OK)
            return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);

        } catch (AuthenticationException e) {
            // Status 401 (Unauthorized)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}