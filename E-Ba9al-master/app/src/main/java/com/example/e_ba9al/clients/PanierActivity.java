package com.example.e_ba9al.clients;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_ba9al.R;
import com.example.e_ba9al.commandes.Commande;
import com.example.e_ba9al.elementsCommande.ElementCommande;
import com.example.e_ba9al.panier.PanierAdapter;
import com.example.e_ba9al.panier.Panier;
import com.example.e_ba9al.session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class PanierActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PanierAdapter panierAdapter;
    private ArrayList<Panier> produits;
    public DatabaseReference reference;
    boolean inActivityPanier;
    boolean clickSupprimer;
    Toolbar toolbar;

    static TextView viewNom;

    public static String userName;
    public static String userNameEpicier;
    static SessionManager sessionManager;
    static HashMap<String, String> user;

    static String timeNow;

    static int prixTotal = 0;
    static int nbreProduits = 0;
    static boolean commandeEnvoyee = false;

    static boolean premiereFoisCommandeEnvoyee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Récupérer l'email
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        user = sessionManager.getUserDetail();
        userName = user.get(sessionManager.USERNAME);
        userNameEpicier = user.get(sessionManager.USERNAMEEPICIER);
        Log.d("idid", "epicier : " + userNameEpicier);

        inActivityPanier = true;
        clickSupprimer = false;
        Log.d("iddi", "inActivityPanier : " + inActivityPanier);
        try{
            premiereFoisCommandeEnvoyee = true;
            getData();
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }
    }
    public void getData(){
        recyclerView = (RecyclerView) findViewById(R.id.itemPanierRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        produits = new ArrayList<>();
        panierAdapter = new PanierAdapter(this, produits);
        recyclerView.setAdapter(panierAdapter);
        Log.d("idid", "au panier : " + userName);
        reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Panier").child(userName);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("iddi", "methode getData : " + inActivityPanier);
                if(inActivityPanier == false) {
                    Log.d("iddi", "liste n'est pas empty");
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    produits = new ArrayList<>();
                    panierAdapter = new PanierAdapter(getApplicationContext(), produits);
                    recyclerView.setAdapter(panierAdapter);
                }
                Log.d("iddi", "apres liste n'est pas empty");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Panier panier = dataSnapshot.getValue(Panier.class);
                    Log.d("iddi", "methode recuperer elements panier : " + panier.getNomProduit());
                    produits.add(panier);
                }
                panierAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void backArticles(View view){
        inActivityPanier = false;

        Intent intent = new Intent(this, ArticlesFragment.class);
        startActivity(intent);
    }

    public void supprimerProduit(View view){
        try{
            inActivityPanier = true;
            Log.d("iddi", "PremierePage");
            clickSupprimer = true;
            if(commandeEnvoyee == false){
                LinearLayout viewParent = (LinearLayout) view.getParent().getParent();
                viewNom = (TextView) viewParent.getChildAt(0);
            }
            DatabaseReference reference;
            recyclerView = (RecyclerView) findViewById(R.id.itemPanierRecyclerView);

            recyclerView.setHasFixedSize(true);
            produits = new ArrayList<>();

            panierAdapter = new PanierAdapter(this, produits);
            recyclerView.setAdapter(panierAdapter);
            reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Panier").child(userName);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if(commandeEnvoyee == false){
                            Log.d("iddi", "methode supprimer");
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Panier panier = dataSnapshot.getValue(Panier.class);
                                String nomProduit = "-" + panier.getNomProduit() + "-";
                                if(nomProduit.equals(viewNom.getText().toString())){
                                    if(inActivityPanier == true){
                                        Log.d("iddi", "methode supprimer : " + panier.getNomProduit());
                                        dataSnapshot.getRef().removeValue();
                                    }
                                }
                            }
                        }
                        else{
                            snapshot.getRef().removeValue();
                            commandeEnvoyee = false;
                        }
                        panierAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        Log.d("iddi", e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }
    }

    public void envoyerCommande(View view) throws ParseException {

        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(PanierActivity.this);

        dialogDelete.setTitle("تحذير!!");
        dialogDelete.setMessage("هل تريد حقا الارسال إلى البقال");
        dialogDelete.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                inActivityPanier = true;
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                timeNow = format.format(date);//On récupère le temps du moment quand on clique sur le bouton "أرسل إلى البقال"
                DatabaseReference commandesProduitsReference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("CommandesProduits").child(userNameEpicier).child(timeNow);
                DatabaseReference commandeReference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Commandes").child(userNameEpicier).child(timeNow);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Panier").child(userName);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        prixTotal = 0;
                        nbreProduits = 0;
                        if(snapshot.exists() && inActivityPanier == true){
                            Log.d("iddi", timeNow);
                            if(premiereFoisCommandeEnvoyee == true){
                                Log.d("iddi", timeNow);
                                premiereFoisCommandeEnvoyee = false;
                                Log.d("iddi", "methode envoyerCommande");
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Panier elementPanier = dataSnapshot.getValue(Panier.class);
                                    ElementCommande produit = new ElementCommande(userName, elementPanier.getNomProduit(), elementPanier.getQuantiteProduit(), elementPanier.getCommentaireProduit(), elementPanier.getImage(), elementPanier.getPrix(), "تم قبول المنتوج");
                                    commandesProduitsReference.push().setValue(produit);

                                    double prixProduit = Double.parseDouble(produit.getPrix()) * Double.parseDouble(produit.getQuantiteProduit());
                                    prixTotal += prixProduit;

                                    nbreProduits++;
                                }
                                Commande commande = new Commande(userName, timeNow, new Double(prixTotal).toString(), new Double(nbreProduits).toString(), new Integer(Commande.PAS_TRAITE).toString());
                                commandeReference.push().setValue(commande);
                                panierAdapter.notifyDataSetChanged();
                                try {
                                    Log.d("iddi", "hhhhhh");
                                    commandeEnvoyee = true;
                                    supprimerProduit(view);
                                }
                                catch (Exception e){
                                    Log.d("iddi", e.getMessage());
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialogDelete.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
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
                PanierActivity.this.finish();
                sessionManager.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        inActivityPanier = false;
        super.onBackPressed();
    }
}