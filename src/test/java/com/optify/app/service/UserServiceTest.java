package com.optify.app.service;

import com.optify.domain.User;
import com.optify.dto.UserPasswordUpdateDto;
import com.optify.dto.UserUpdateDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.repository.UserRepository;
import com.optify.services.UserService;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUserProfile() throws DataException {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setMail("old@example.com");

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("new@example.com");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        userService.updateUserProfile(username, userUpdateDto);

        // Assert
        assertEquals("new@example.com", user.getMail());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testUpdateUserProfile_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        UserUpdateDto userUpdateDto = new UserUpdateDto();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataException.class, () -> userService.updateUserProfile(username, userUpdateDto));
    }

    @Test
    void testChangeUserPassword_InvalidCurrentPassword() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedOldPassword");

        UserPasswordUpdateDto passwordUpdateDto = new UserPasswordUpdateDto();
        passwordUpdateDto.setCurrentPassword("wrongPassword");
        passwordUpdateDto.setNewPassword("newPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(encoder.matches("wrongPassword", "encodedOldPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> userService.changeUserPassword(username, passwordUpdateDto));
    }

}
