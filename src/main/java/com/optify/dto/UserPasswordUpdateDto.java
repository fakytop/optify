package com.optify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserPasswordUpdateDto {
    @NotBlank(message = "The current password cannot be empty.")
    private String currentPassword;

    @NotBlank(message = "The new password cannot be empty.")
    @Size(min = 8, message = "The new password must be at least 8 characters long.")
    private String newPassword;

    // Getters and setters
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
