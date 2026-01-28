package com.optify.controllers;

import com.optify.dto.UserPasswordUpdateDto;
import com.optify.dto.UserRegisterDto;
import com.optify.dto.UserLoginDto;
import com.optify.dto.UserUpdateDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    @PostMapping("/updateProfile")
    @SecurityRequirement(name = "ApiKeyAuth")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> updateUserProfile(Authentication auth, @RequestBody UserUpdateDto userUpdateDto) {
        try {
            String username = auth.getName(); // Obtiene el username del token JWT
            instance.updateUserProfile(username, userUpdateDto);
            return ResponseEntity.ok("[UPDATE] User profile updated successfully.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/changePassword")
    @SecurityRequirement(name = "ApiKeyAuth")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> changeUserPassword(Authentication auth, @RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {
        try {
            String username = auth.getName(); // Obtiene el username del token JWT
            instance.changeUserPassword(username, userPasswordUpdateDto);
            return ResponseEntity.ok("[UPDATE] Password changed successfully.");
        } catch (AuthenticationException | DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
