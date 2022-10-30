package com.example.e_ba9al.Model;

public class Epicier {
    private String userName , PhoneNumber , password ;

    public Epicier() {
    }

    public Epicier(String userName, String phoneNumber, String password) {
        this.userName = userName;
        PhoneNumber = phoneNumber;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
