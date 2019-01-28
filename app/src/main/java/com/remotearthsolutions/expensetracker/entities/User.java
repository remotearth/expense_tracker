package com.remotearthsolutions.expensetracker.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private int userId;
    private String userName;
    private String authType;

    public User() {
    }

    public User(int userId, String userName, String authType) {
        this.userId = userId;
        this.userName = userName;
        this.authType = authType;
    }

    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        authType = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(authType);
    }
}
