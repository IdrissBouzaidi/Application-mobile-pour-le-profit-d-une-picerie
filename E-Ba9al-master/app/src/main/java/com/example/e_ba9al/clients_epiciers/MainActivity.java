package com.example.e_ba9al.clients_epiciers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_ba9al.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void faireAuthentifier(View view){
        Intent intent = new Intent(MainActivity.this, AuthentificationActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.clientBtn:
                bundle.putString("statue", "client");
                break;
            case R.id.epicierBtn:
                bundle.putString("statue", "epicier");
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
