package com.optify.controllers;

import com.optify.dto.*;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Facade instance;

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody UserLoginDto userDto) {
        try {
            UserResponseDto userResp = instance.logIn(userDto);
            return ResponseEntity.ok(userResp);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDto) {
        try {
            UserResponseDto userResp = instance.register(userRegisterDto);
            return ResponseEntity.ok(userResp);
        } catch (AuthenticationException | DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/updateProfile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
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
