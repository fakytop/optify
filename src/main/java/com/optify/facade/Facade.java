package com.optify.facade;

import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;
import com.optify.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Facade {

    @Autowired
    private UserService userService;

    private Facade() {}

    public User signIn(User user) throws AuthenticationException {
        return userService.signIn(user);
    }

    public User logIn(String username, String password) throws AuthenticationException {
        return userService.logIn(username, password);
    }
}
