package com.optify.dto;

import com.optify.domain.User;

public class UserResponseDto {
    private String name;
    private String lastName;
    private String mail;
    private long storeRut;
    private int preferredDay;
    private String role;
    private String token;

    public UserResponseDto() {
    }

    public UserResponseDto(User user, String token) {
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.mail = user.getMail();
        this.storeRut = user.getPreferredStore().getRut();
        this.preferredDay = user.getPreferredDay();
        this.role = user.getRole().toString();
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getStoreRut() {
        return storeRut;
    }

    public void setStoreRut(long storeRut) {
        this.storeRut = storeRut;
    }

    public int getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(int preferredDay) {
        this.preferredDay = preferredDay;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
