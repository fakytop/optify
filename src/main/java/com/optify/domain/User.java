package com.optify.domain;

import com.optify.dto.UserDto;
import com.optify.exceptions.AuthenticationException;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    private long ci;
    private String name;
    private String lastName;
    private String username;
    private String mail;
    private String password;
//    private City city;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "preferred_store_rut")
    private Store preferredStore;
    private int preferredDay;

    public int getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(int preferredDay) {
        this.preferredDay = preferredDay;
    }

    public Store getPreferredStore() {
        return preferredStore;
    }

    public void setPreferredStore(Store preferredStore) {
        this.preferredStore = preferredStore;
    }

    public User() {
        this.cart = new Cart();
        cart.setUser(this);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void validPassword() throws AuthenticationException {
        if(password.length() < 8) {
            throw new AuthenticationException("[Authentication] La contraseña no cumple con los requisitos minimos.");
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "ci=" + ci +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + username + '\'' +
                ", eMail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", cart=" + cart +
                ", preferredStore=" + preferredStore +
                ", preferredDay=" + preferredDay +
                '}';
    }

    public void setRegisterData(UserDto userDto, Store preferredStore) {
        this.name = userDto.getUserName();
        this.lastName = userDto.getUserLastName();
        this.username = userDto.getUserUsername();
        this.ci = userDto.getUserCi();
        this.mail = userDto.getUserMail();
        this.password = userDto.getUserPassword();
        this.preferredStore = preferredStore;
        this.preferredDay = userDto.getUserPreferredDay();
    }
}
