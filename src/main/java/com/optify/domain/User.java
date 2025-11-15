package com.optify.domain;

import com.optify.exceptions.AuthenticationException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    private long ci;
    private String name;
    private String lastName;
    private String userName;
    private String eMail;
    private String password;
    private City city;
    private Cart cart = new Cart();
    private Store preferredStore;
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

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void validPassword(String password) throws AuthenticationException {
        if(password.length() < 8) {
            throw new AuthenticationException("[Authentication] La contraseña no cumple con los requisitos minimos.");
        }

    }
}
