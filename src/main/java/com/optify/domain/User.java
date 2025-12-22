package com.optify.domain;

import com.optify.exceptions.AuthenticationException;
import jakarta.persistence.*;

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
                ", userName='" + userName + '\'' +
                ", eMail='" + eMail + '\'' +
                ", password='" + password + '\'' +
                ", cart=" + cart +
                ", preferredStore=" + preferredStore +
                ", preferredDay=" + preferredDay +
                '}';
    }
}
