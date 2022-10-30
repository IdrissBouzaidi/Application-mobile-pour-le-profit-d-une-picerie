package com.example.e_ba9al.clients;

import static com.example.e_ba9al.clients.AccueilClientFragment.getDataFavoris;
import static com.example.e_ba9al.clients.AccueilClientFragment.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.e_ba9al.R;
import com.example.e_ba9al.panier.Panier;
import com.example.e_ba9al.produits.Produit;
import com.example.e_ba9al.produits.ProduitAdapter;
import com.example.e_ba9al.session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AjouterAuPanierActivity extends AppCompatActivity {

    String categorie;
    boolean existeDansFavoris;//Indique si on a cliqué sur l'icone de favoris
    StringBuffer SBNomProduit;
    Query queryFavoris;
    public static String userName;
    public static boolean etatFavorsChangee = false;//Si on clique sur le bouton favoirs, alors cette variable va devenir false.

    SessionManager sessionManager;

    TextView nomProduit;
    TextView prixProduit;
    ImageView imageProduit;
    ImageView favoris;
    EditText commentaireProduit;
    Toolbar toolbar;
    TextView viewQuantite;


    private RecyclerView recyclerView;
    private ProduitAdapter produitsAdapter;
    private ArrayList<Produit> produits;
    public DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_au_panier);


            viewQuantite = findViewById(R.id.quantiteProduit);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //Récupérer l'email
            sessionManager = new SessionManager(this);
            sessionManager.checkLogin();
            HashMap<String, String> user = sessionManager.getUserDetail();
            userName = user.get(sessionManager.USERNAME);
            Log.d("idid", userName);

            imageProduit = findViewById(R.id.imageProduit);
            nomProduit = findViewById(R.id.nomProduit);
            prixProduit = findViewById(R.id.prixProduit);
            favoris = findViewById(R.id.favoris);
            commentaireProduit = findViewById(R.id.commentaireProduit);


            Bundle bundle = getIntent().getExtras();
            Log.d("idrisss", bundle.getString("nomProduit"));
            Log.d("idrisss", bundle.getString("nomCategorie"));
            categorie = bundle.getString("nomCategorie");
            reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Categorie").child(categorie);
        try{
            Query query = reference.orderByChild("nom");
            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                Produit produit = dataSnapshot.getValue(Produit.class);
                                //Log.d("anass", produit.getNom());
                                String nomProduitExtrait = "-" + produit.getNom() + "-";
                                Log.d("iddi", "nom de notre produit : " + bundle.getString("nomProduit"));
                                Log.d("iddi", "nom categorie : " + categorie);
                                Log.d("iddi", "nom produit : " + nomProduitExtrait);
                                if (nomProduitExtrait.equals(bundle.getString("nomProduit"))) {
                                    nomProduit.setText("-" + produit.getNom() + "-");
                                    prixProduit.setText("-" + produit.getPrix() + " درهم-");
                                    Glide.with(AjouterAuPanierActivity.this).load(produit.getImage()).into(imageProduit);
                                }

                            } catch (Exception e) {
                                Log.d("iddi", e.getMessage());
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
            //Pour connaitre si le produit est dans la liste des favoris.
            SBNomProduit = new StringBuffer(bundle.getString("nomProduit"));
            SBNomProduit.deleteCharAt(0);
            SBNomProduit.deleteCharAt(SBNomProduit.length() - 1);
            DatabaseReference referenceFavoris = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Favoris").child(userName);
            queryFavoris = referenceFavoris;
            etatImageFavorisCorrespondant();//Choisir entre les 2 images de favoris
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }

    }



    public void incrQuantite(View view){
        try{
            int quantite = Integer.parseInt(viewQuantite.getText().toString()) + 1;
            viewQuantite.setText(new Integer(quantite).toString());
        }
        catch (Exception e){
            Log.d("adde", e.getMessage());
        }
    }
    public void decrQuantite(View view){
        int quantite = Integer.parseInt(viewQuantite.getText().toString()) - 1;
        if(quantite == -1)
            quantite = 0;
        viewQuantite.setText(new Integer(quantite).toString());
    }
    public void ajouterAuPanier(View view){
        String quantite = viewQuantite.getText().toString();
        if(!quantite.equals("0")){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Panier").child(userName);
            Query query = reference.orderByChild("nom");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Log.d("iddi", "exists");
                        boolean findElementOnDBPanier = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                Panier panierComplet = dataSnapshot.getValue(Panier.class);
                                String nomPanierBD = "-" + panierComplet.getNomProduit() + "-";
                                if (nomPanierBD.equals(nomProduit.getText())) {
                                    findElementOnDBPanier = true;
                                    int quantiteTotal = Integer.parseInt(panierComplet.getQuantiteProduit()) + Integer.parseInt(quantite);
                                    dataSnapshot.getRef().child("quantiteProduit").setValue(new Integer(quantiteTotal).toString());
                                    dataSnapshot.getRef().child("commentaireProduit").setValue(commentaireProduit.getText().toString());
                                    break;
                                }
                            } catch (Exception e) {
                                Log.d("iddi", e.getMessage());
                            }
                        }
                        if (findElementOnDBPanier == false) {
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Categorie");
                            reference2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                            Produit produit = dataSnapshot2.getValue(Produit.class);
                                            String nom = "-" + produit.getNom() + "-";
                                            if (nom.equals(nomProduit.getText())) {
                                                Panier panierElement = new Panier(produit.getNom(), viewQuantite.getText().toString(), commentaireProduit.getText().toString(), produit.getImage(), produit.getPrix());
                                                reference.push().setValue(panierElement);

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
                    else{
                        Log.d("iddi", "no");
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Categorie");
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {
                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                        Produit produit = dataSnapshot2.getValue(Produit.class);
                                        String nom = "-" + produit.getNom() + "-";
                                        if (nom.equals(nomProduit.getText())) {

                                            Log.d("iddi", "1");
                                            Panier panierElement = new Panier(produit.getNom(), viewQuantite.getText().toString(), commentaireProduit.getText().toString(), produit.getImage(), produit.getPrix());

                                            reference.push().setValue(panierElement);

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

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Intent intent;
            Log.d("iddi", "Page precedente : " + getIntent().getExtras().getString("pagePrecedente"));
            if(getIntent().getExtras().getString("pagePrecedente").equals("Accueil"))
                intent = new Intent(this, AccueilClientActivity.class);
            else
                intent = new Intent(this, ProduitsActivity.class);
            Bundle bundle = new Bundle();
            Log.d("iddi", "passer de l'activité AjouterAuPanier à l'activité suivante, la catégorie : " + categorie);
            bundle.putString("nomCategorie", categorie);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }



    public void etatImageFavorisCorrespondant(){
        queryFavoris.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                existeDansFavoris = false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue().equals(SBNomProduit.toString())){
                        Log.d("iddi", "Existe dans la favoris");
                        Glide.with(AjouterAuPanierActivity.this).load(R.drawable.ic_baseline_star_rate_24).into(favoris);
                        existeDansFavoris = true;
                    }
                }
                if(existeDansFavoris == false) {
                    Glide.with(AjouterAuPanierActivity.this).load(R.drawable.ic_baseline_star_border_24).into(favoris);
                    Log.d("iddi", "N'existe pas dans la favoris");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void ajouterAuFavoris(View view){//Par cette méthode, on peut ajouter ou retirer de la favoris.
        etatFavorsChangee = true;
        DatabaseReference referenceFavoris = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Favoris").child(userName);
        Query query = referenceFavoris;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(existeDansFavoris == false) {
                    referenceFavoris.push().setValue(SBNomProduit.toString());
                    Glide.with(AjouterAuPanierActivity.this).load(R.drawable.ic_baseline_star_rate_24).into(favoris);
                    existeDansFavoris = true;
                }
                else{
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if(dataSnapshot.getValue().equals(SBNomProduit.toString())) {
                            dataSnapshot.getRef().removeValue();
                            Glide.with(AjouterAuPanierActivity.this).load(R.drawable.ic_baseline_star_border_24).into(favoris);
                        }
                        existeDansFavoris = false;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                Intent intent = new Intent(AjouterAuPanierActivity.this, PanierActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemLogout:
                try{
                    AjouterAuPanierActivity.this.finish();
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
        if(etatFavorsChangee == true)
            getDataFavoris(view);
        super.onBackPressed();
    }
}