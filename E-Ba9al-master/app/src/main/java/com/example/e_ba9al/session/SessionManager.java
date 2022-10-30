package com.example.e_ba9al.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.e_ba9al.clients_epiciers.AuthentificationActivity;
import com.example.e_ba9al.clients_epiciers.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USERNAME = "USERNAME";
    public static final String STATUE = "STATUE";
    public static final String USERNAMEEPICIER = "USERNAMEEPICIER";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String userName, int statue, String userNAmeEpicier){
        //statut = 0 => client
        //       = 1 => epicier
        //userNameEpicier : on l'utilise pour récupérer l'username de l'épicier correspondant à un utilisateur
        //userNameEpicier = "" => statut = epicier
        editor.putBoolean(LOGIN, true);
        editor.putString(USERNAME, userName);
        editor.putString(STATUE, new Integer(statue).toString());
        editor.putString(USERNAMEEPICIER, userNAmeEpicier);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLogin()){
            Intent intent = new Intent(context, AuthentificationActivity.class);
            context.startActivity(intent);
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(STATUE, sharedPreferences.getString(STATUE, null));
        user.put(USERNAMEEPICIER, sharedPreferences.getString(USERNAMEEPICIER, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
