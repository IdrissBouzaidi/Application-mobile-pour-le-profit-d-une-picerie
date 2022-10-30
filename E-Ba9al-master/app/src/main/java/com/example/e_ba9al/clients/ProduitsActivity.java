package com.example.e_ba9al.clients;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_ba9al.R;
import com.example.e_ba9al.produits.Produit;
import com.example.e_ba9al.produits.ProduitAdapter;
import com.example.e_ba9al.session.SessionManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;


public class ProduitsActivity extends AppCompatActivity {
    TextView nomCategorie;
    EditText bareRecherche;
    public static String categorie;
    private RecyclerView recyclerView;
    private ProduitAdapter produitsAdapter;
    private ArrayList<Produit> produits;
    public DatabaseReference reference;

    SessionManager sessionManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_produits);

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //Récupérer l'email
            sessionManager = new SessionManager(this);
            sessionManager.checkLogin();
            HashMap<String, String> user = sessionManager.getUserDetail();
            String userName = user.get(sessionManager.USERNAME);
            Log.d("idid", userName);
            Bundle bundle = getIntent().getExtras();
            Log.d("iddi", bundle.getString("nomCategorie"));
            nomCategorie = (TextView) findViewById(R.id.nomCategorie);
            categorie = bundle.getString("nomCategorie");
            if(categorie.equals("boisson"))
                nomCategorie.setText("مشروبات");
            else if(categorie.equals("graines"))
                nomCategorie.setText("قطنيات");
            else if(categorie.equals("laits"))
                nomCategorie.setText("حليب و مشتقاته");
            else if(categorie.equals("hygiene"))
                nomCategorie.setText("مواد التنظيف");
            else if(categorie.equals("cookies"))
                nomCategorie.setText("حلويات");
            else if(categorie.equals("autres"))
                nomCategorie.setText("منتوجات اخرى");
            bareRecherche = findViewById(R.id.rechercheProduit);
            rechercherProduit(bareRecherche.getText().toString());


            bareRecherche.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    rechercherProduit(bareRecherche.getText().toString());
                }
            });

        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }

    }

    public void rechercherProduit(String nomRecherchee){
        try{
            categorie = getIntent().getExtras().getString("nomCategorie");
            FirebaseApp.initializeApp(this);
            recyclerView = findViewById(R.id.itemProduit);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            //firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Categorie").child(categorie);
            produits = new ArrayList<>();

            //Clear ArrayList
            ClearAll();


            Query query = reference.orderByValue();
            Log.d("iddi", "hhhhhh");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ClearAll();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("iddi", "methodeRechercheProduit");
                        Produit produit = snapshot.getValue(Produit.class);
                        if(produit.getNom().contains(nomRecherchee)) {
                            Log.d("iddi", produit.getNom());
                            produits.add(produit);
                        }
                    }
                    produitsAdapter  = new ProduitAdapter(getApplicationContext(), produits);
                    recyclerView.setAdapter(produitsAdapter);
                    produitsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //categorie = null;
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }




    }
    public void ClearAll(){
        if(produits != null){
            produits.clear();
            if(produitsAdapter != null){
                produitsAdapter.notifyDataSetChanged();
            }
        }
        produits = new ArrayList<>();
    }

    public void allerAuPanier(View view){
        Intent intent = new Intent(ProduitsActivity.this, PanierActivity.class);
        startActivity(intent);
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
                Intent intent = new Intent(ProduitsActivity.this, PanierActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemLogout:
                try{
                    ProduitsActivity.this.finish();
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
    @Override
    public void onBackPressed() {//Quand on clique sur le Back Button, on ne va pas appliquer ce qui s'applique d'abitude, mais on va le forcer à faire d'autres choses.
        Intent intent = new Intent(this, AccueilClientActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fromListProduits", "");
        intent.putExtras(bundle);
        startActivity(intent);
    }







}