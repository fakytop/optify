package com.optify.controllers;

import com.optify.dto.UserRegisterDto;
import com.optify.dto.UserLoginDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private Facade instance;

    @SecurityRequirement(name = "ApiKeyAuth")
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody UserLoginDto userDto) {
        try {
            String token = instance.logIn(userDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @SecurityRequirement(name = "ApiKeyAuth")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDto) {
        try {
            instance.register(userRegisterDto);
            return ResponseEntity.ok("[REGISTER] Usuario registrado con éxito.");
        } catch (AuthenticationException | DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
