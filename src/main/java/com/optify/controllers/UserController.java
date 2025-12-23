package com.optify.controllers;

import com.optify.dto.UserDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
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

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody UserDto userDto) {
        try {
            String token = instance.logIn(userDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            instance.register(userDto);
            return ResponseEntity.ok("[REGISTER] Usuario registrado con éxito.");
        } catch (AuthenticationException | DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
