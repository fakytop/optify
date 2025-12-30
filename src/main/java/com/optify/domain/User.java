package com.optify.domain;

import com.optify.dto.UserDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
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

    public Cart getCart() {
        return cart;
    }

    public void addItemToCart(CartItem cartItem) {
        cart.addItem(cartItem);
    }

    public void validatePassword() throws AuthenticationException {
        if(password.length() < 8) {
            throw new AuthenticationException("[Authentication] La contraseña no cumple con los requisitos minimos.");
        }

        // (?=.*[0-9])       -> Al menos un número
        // (?=.*[a-z])       -> Al menos una minúscula
        // (?=.*[A-Z])       -> Al menos una mayúscula
        // (?=.*[@#$%^&+=!]) -> Al menos un carácter especial
        // \\S+$             -> Sin espacios en blanco
        String passwordPattern = "^(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[@#$%^&+=!])" +
                "(?=\\S+$).{8,}$";
        if(!password.matches(passwordPattern)) {
            throw new AuthenticationException("[Authentication] La contraseña debe incluir mayúsculas, " +
                    "minúsculas, numeros, caracteres especiales y no puede contener espacios.");
        }

    }

    public void validateCi() throws AuthenticationException {
        String ciStr = String.valueOf(ci);
        if(ciStr.length() < 7 || ciStr.length() > 8) {
            throw new AuthenticationException("[Authentication] Cédula no válida. Debe incluir el dígito verificador sin puntos ni guiones.");
        }

        if(ciStr.length() == 7) {
            ciStr = "0" + ciStr;
        }

        int verificationDigit = Character.getNumericValue(ciStr.charAt(7));
        String ciBody = ciStr.substring(0, 7);

        int calculatedDigit = calculateVerificationDigit(ciBody);

        if(verificationDigit != calculatedDigit) {
            throw new AuthenticationException("[Authentication] El dígito verificador no es correcto.");
        }
    }

    private int calculateVerificationDigit(String ciBody) {
        int[] constants = {2,9,8,7,6,3,4};
        int sum = 0;
        for(int i = 0; i < constants.length; i++) {
            int digit = Character.getNumericValue(ciBody.charAt(i));
            sum += digit*constants[i] % 10;
        }

        int modResult = sum % 10;
        return (modResult == 0) ? 0 : (10 - modResult);
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

    public void setRegisterData(UserDto userDto, Store preferredStore) throws AuthenticationException {
        this.name = userDto.getUserName();
        this.lastName = userDto.getUserLastName();
        this.username = userDto.getUserUsername();
        this.ci = userDto.getUserCi();
        validateCi();
        this.mail = userDto.getUserMail();
        this.password = userDto.getUserPassword();
        validatePassword();
        this.preferredStore = preferredStore;
        this.preferredDay = userDto.getUserPreferredDay();
    }

    public void removeItemFromCart(CartItem cartItem) throws DataException {
        this.cart.removeItem(cartItem);
    }
}
