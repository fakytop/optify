package com.optify.dto;

import com.optify.domain.Store;
import org.jspecify.annotations.Nullable;

public class UserDto {
    private String userName;
    private String userPassword;
    private long userCi;
    private String UserUsername;
    private String userLastName;
    private String userMail;
    private long userPreferredStore;
    private int userPreferredDay;

    public UserDto() {
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
        this.UserUsername = userUsername;
    }

    public String getUserUsername() {
        return UserUsername;
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
