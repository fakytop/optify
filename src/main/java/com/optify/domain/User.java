package com.optify.domain;

import com.optify.exceptions.AuthenticationException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "users")
public class User {
    @Id
    private long ci;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name") // Mapeo a la columna correcta en la DB (snake_case)
    private String lastName;

    private String userName; // Mapea automáticamente a user_name

    @Column(name = "mail") // Mapeo explícito a la columna 'mail' en la DB
    private String mail;

    private String password;
    //    private City city;
    //    private Cart cart = new Cart();
    //    private Store preferredStore;
    private int preferredDay;

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCi() {
        return ci;
    }

    public void setCi(long ci) {
        this.ci = ci;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // CORREGIDO: Setter y Getter para lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // CORREGIDO: Getter y Setter para mail (simple)
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    //    public City getCity() {
    //      return city;
    //    }

    //    public void setCity(City city) {
    //        this.city = city;
    //    }

    public void validPassword() throws AuthenticationException {
        if(password.length() < 8) {
            throw new AuthenticationException("[Authentication] La contraseña no cumple con los requisitos minimos.");
        }

    }
}