package com.example.e_ba9al.epiciers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.e_ba9al.R;
import com.example.e_ba9al.fragmentsAdapter.FragmentsAdapter;
import com.example.e_ba9al.session.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

public class AccueilEpicierActivity extends AppCompatActivity {


    SessionManager sessionManager;
    Toolbar toolbar;

    public static String userName;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_epicier);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Récupérer l'email
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userName = user.get(sessionManager.USERNAME);
        Log.d("idid", userName);
        Log.d("iddi", "username : " + userName);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        FragmentsAdapter fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager());
        fragmentsAdapter.addFragment(new CommandesFragment(), "الطلبات");
        fragmentsAdapter.addFragment(new CreditsFragment(), "الكارني");
        viewPager.setAdapter(fragmentsAdapter);
        //L'ajout des images correspondantes aux tab du menu du fragment
        tabLayout.getTabAt(0).setIcon(R.drawable.commande);
        tabLayout.getTabAt(1).setIcon(R.drawable.carnet);
        tabLayout.getTabAt(1).select();

    }

    @Override
    public void onBackPressed() {//Quand on clique sur le Back Button, on ne va pas appliquer ce qui s'applique d'abitude, mais on va le forcer à faire d'autres choses.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_epicier, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        switch(idItem){
            case R.id.itemLogout:
                AccueilEpicierActivity.this.finish();
                sessionManager.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}