package com.example.e_ba9al.Assist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Help<satatic> {

    public static final String appAaddress = "@Hanout.app";


    public static boolean isValidUserName(String userNameInput) {
        Pattern ptrn = Pattern.compile("^([a-z]|[A-z]){4,12}$");
        Matcher match = ptrn.matcher(userNameInput);

        if(!match.matches()){
            return false;
        }else{
            // we should check if this user already exist
            return true;
        }
    }

    public static boolean isValidPassword(String passwordInput) {
        Pattern ptrn = Pattern.compile("^([a-z]|[A-z]ّ|[0-9]){6,12}$");
        Matcher match = ptrn.matcher(passwordInput);

        if(!match.matches()){
            return false;
        }else{

            return true;
        }
    }

    public static boolean isValidPhoneNum(String clientPhoneInput) {
        Pattern ptrn = Pattern.compile("^(06|07)[0-9]{8}$");
        Matcher match = ptrn.matcher(clientPhoneInput);

        return match.matches();

    }
}
