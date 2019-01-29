package com.remotearthsolutions.expensetracker.entities;

import org.parceler.Parcel;

@Parcel
public class User {

    int userId;
    String userName;
    String authType;

    public User() {
    }

    public User(String userName, String authType) {
        this.userName = userName;
        this.authType = authType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
