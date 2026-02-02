package com.optify.dto;

import jakarta.validation.constraints.Email;

public class UserUpdateDto {
    @Email(message = "El mail debe ser válido")
    private String email;

    private Integer userPreferredDay;

    private Long userPreferredStore;

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserPreferredDay() {
        return userPreferredDay;
    }

    public void setUserPreferredDay(Integer userPreferredDay) {
        this.userPreferredDay = userPreferredDay;
    }

    public Long getUserPreferredStore() {
        return userPreferredStore;
    }

    public void setUserPreferredStore(Long userPreferredStore) {
        this.userPreferredStore = userPreferredStore;
    }
}
