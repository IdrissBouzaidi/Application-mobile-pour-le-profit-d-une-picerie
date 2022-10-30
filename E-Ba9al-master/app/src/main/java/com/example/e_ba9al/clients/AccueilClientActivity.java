package com.example.e_ba9al.clients;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.e_ba9al.R;
import com.example.e_ba9al.fragmentsAdapter.FragmentsAdapter;
import com.example.e_ba9al.produits.Produit;
import com.example.e_ba9al.session.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AccueilClientActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManager sessionManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_client);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Récupérer l'email
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userName = user.get(sessionManager.USERNAME);
        String epicier = user.get(sessionManager.USERNAMEEPICIER);
        Log.d("iddi", "username : " + userName);
        Log.d("iddi", "epicier : " + epicier);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        FragmentsAdapter fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager());
        fragmentsAdapter.addFragment(new ArticlesFragment(), "المنتوجات");
        fragmentsAdapter.addFragment(new AccueilClientFragment(), "الرئيسية");
        fragmentsAdapter.addFragment(new NotificationsFragment(), "الإشعارات");
        viewPager.setAdapter(fragmentsAdapter);
        //L'ajout des images correspondantes aux tab du menu du fragment
        tabLayout.getTabAt(0).setIcon(R.drawable.boisson);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_notifications_24);
        if(getIntent().getExtras() != null && getIntent().getExtras().getString("nomCategorie") == null)//Le cas dans lequel on vient de l'activité ListProduits. et on ne vient pas de l'activité AjouterAuPanier
            tabLayout.getTabAt(0).select();
        else//Le cas dans lequel on vient de l'activité Login ou RegistreClient
            tabLayout.getTabAt(1).select();

    }
    @Override
    public void onBackPressed() {//Quand on clique sur le Back Button, on ne va pas appliquer ce qui s'applique d'abitude, mais on va le forcer à faire d'autres choses.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        switch(idItem){
            case R.id.itemPanier:
                Intent intent = new Intent(AccueilClientActivity.this, PanierActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemLogout:
                try{
                    AccueilClientActivity.this.finish();
                    sessionManager.logout();
                }
                catch(Exception e){
                    Log.d("idid", e.getMessage());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static void passerALaPageAjouterAuPanier(View view, Context context, Intent intent, Bundle bundle, TextView nomProduit){
        Log.d("iddi", "mathode passerALaPageAjouterAuPanier");

        bundle.putString("nomProduit", nomProduit.getText().toString());
        bundle.putString("pagePrecedente", "Accueil");



        FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Categorie").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("iddi", "mathode passerALaPageAjouterAuPanier");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        Log.d("iddi", "hahahaha");
                        Produit produit = dataSnapshot2.getValue(Produit.class);
                        String nomProduitCourant = "-" + produit.getNom() + "-";
                        Log.d("iddi", "nom produit courant : " + nomProduitCourant);
                        Log.d("iddi", "nom produit : " + nomProduit.getText().toString());
                        if(nomProduitCourant.equals(nomProduit.getText().toString())){
                            Log.d("iddi", "hohohoho");
                            bundle.putString("nomCategorie", dataSnapshot.getKey());
                            intent.putExtras(bundle);
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.getApplicationContext().startActivity(intent);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}