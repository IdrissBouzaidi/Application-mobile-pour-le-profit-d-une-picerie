package com.example.e_ba9al.utilisateurs;

import java.io.Serializable;

public class Client implements Serializable {
    private String userName , phoneNumber ,password ,epicierPhoneNumber;

    public Client() {
    }

    public Client(String userName , String phoneNumber , String password , String epicierPhoneNumber) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.epicierPhoneNumber = epicierPhoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEpicierPhoneNumber() {
        return epicierPhoneNumber;
    }

    public void setEpicierPhoneNumber(String epicierPhoneNumber) {
        this.epicierPhoneNumber = epicierPhoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
