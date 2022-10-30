package com.example.e_ba9al.clients_epiciers;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_ba9al.R;

public class WelcomScreenActivity extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom_screen);

        new Handler().postDelayed(()-> {
            Intent intent = new Intent(WelcomScreenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        },5000);
    }
}