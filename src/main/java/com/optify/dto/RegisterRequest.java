package com.optify.dto;

public class RegisterRequest {
    private long ci;
    private String name;
    private String lastName;
    private String userName;
    private String mail; // CORREGIDO a minúsculas
    private String password;

    // Getters
    public long getCi() { return ci; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getUserName() { return userName; }
    public String getMail() { return mail; } // Coincide con el campo
    public String getPassword() { return password; }

    // Setters
    public void setCi(long ci) { this.ci = ci; }
    public void setName(String name) { this.name = name; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setMail(String mail) { this.mail = mail; } // Coincide con el campo
    public void setPassword(String password) { this.password = password; }
}