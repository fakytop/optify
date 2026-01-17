package com.optify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserRegisterDto {
    private String userUsername;
    @Schema(description = "Contraseña: Debe tener al menos 1 mayúscula, 1 mínuscula, 1 número, sin espacios y un carácter especial {@#$%^&+=!}",
    example = "Admin1234!",
    minLength = 8,
    format = "password")
    private String userPassword;
    @Schema(description = "Cédula de identidad: Sin puntos ni guiones, incluye dígito verificador.",
            example = "5042053-7 => 50420537")
    private long userCi;
    private String userName;
    private String userLastName;
    private String userMail;
    @Schema(description = "Rut de la tienda preferida.")
    private long userPreferredStore;
    @Schema(description = "Día de la semana: indicar el día de la semana preferido para hacer sus compras.",
    minimum = "0 - Domingo",
    maximum = "6 - Sábado")
    private int userPreferredDay;

    public UserRegisterDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserCi(long userCi) {
        this.userCi = userCi;
    }

    public long getUserCi() {
        return userCi;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserPreferredStore(long userPreferredStore) {
        this.userPreferredStore = userPreferredStore;
    }

    public long getUserPreferredStore() {
        return userPreferredStore;
    }

    public void setUserPreferredDay(int userPreferredDay) {
        this.userPreferredDay = userPreferredDay;
    }

    public int getUserPreferredDay() {
        return userPreferredDay;
    }
}
