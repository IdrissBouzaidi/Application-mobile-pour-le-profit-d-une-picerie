package com.example.e_ba9al.epiciers;

import static com.example.e_ba9al.commandes.Commande.TRAITE;
import static com.example.e_ba9al.elementsCommande.ElementCommandeAdapter.ACCEPTE;
import static com.example.e_ba9al.elementsCommande.ElementCommandeAdapter.REFUSE;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_ba9al.R;
import com.example.e_ba9al.elementsCommande.ElementCommande;
import com.example.e_ba9al.elementsCommande.ElementCommandeAdapter;
import com.example.e_ba9al.reponses.Reponse;
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


public class CommandeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ElementCommandeAdapter produitAdapter;
    private ArrayList<ElementCommande> produits;
    private Context context;
    public DatabaseReference reference;
    //private ElementCommandeAdapter.RecyclerViewClickListener listener;
    Toolbar toolbar;

    Button envoyerReponse;
    RecyclerView itemProduitsCommandeRecyclerView;

    public static String userName;
    SessionManager sessionManager;

    static String timeCommande;
    static String anneesCommande;
    static String moisCommande;
    static String tempsCommande;

    static String nomClient;
    public static String situation;

    public static boolean dansMehodeChangerValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        try{
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //Récupérer l'email
            sessionManager = new SessionManager(this);
            sessionManager.checkLogin();
            HashMap<String, String> user = sessionManager.getUserDetail();
            userName = user.get(sessionManager.USERNAME);
            Log.d("idid", userName);

            Bundle bundle = getIntent().getExtras();

            //Pour récupérer la date et le userNAme du client
            timeCommande = bundle.getString("timeCommande");
            anneesCommande = timeCommande.substring(0, 4);
            moisCommande = timeCommande.substring(5, 7);
            tempsCommande = timeCommande.substring(8);
            nomClient = bundle.getString("nomClient");
            situation = bundle.getString("situation");
            envoyerReponse = findViewById(R.id.envoyerReponse);
            itemProduitsCommandeRecyclerView = findViewById(R.id.itemProduitsCommandeRecyclerView);
            if(Integer.parseInt(situation) == TRAITE) {
                envoyerReponse.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)itemProduitsCommandeRecyclerView.getLayoutParams();
                params.setMargins(0, 0, 0, 20); //substitute parameters for left, top, right, bottom
                itemProduitsCommandeRecyclerView.setLayoutParams(params);
            }


            FirebaseApp.initializeApp(this);
            //clickOniTem();
            recyclerView = findViewById(R.id.itemProduitsCommandeRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            //firebase
            reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("CommandesProduits").child(userName);
            produits = new ArrayList<>();

            //Clear ArrayList
            ClearAll();

            //Firebase
            getData();
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }
    }

    public void getData(){
        dansMehodeChangerValidation = false;
        Query query = reference.orderByValue();
        Log.d("iddi", "hhhhhh");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("iddi", "HAAAHIII : " + dansMehodeChangerValidation);
                if(dansMehodeChangerValidation == false){
                    ClearAll();
                    for(DataSnapshot snapshot1: dataSnapshot.getChildren()) {
                        if(snapshot1.getKey().equals(anneesCommande)){
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                if(snapshot2.getKey().equals(moisCommande)){
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        if(snapshot3.getKey().equals(tempsCommande)){
                                            for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                                ElementCommande produit = snapshot4.getValue(ElementCommande.class);
                                                try {
                                                    if(produit.getNomClient().equals(nomClient))
                                                        produits.add(produit);
                                                }
                                                catch (Exception e){
                                                    Log.d("iddi", e.getMessage());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    produitAdapter  = new ElementCommandeAdapter(getApplicationContext(), produits);
                    recyclerView.setAdapter(produitAdapter);
                    produitAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ClearAll(){
        if(produits != null){
            produits.clear();
            if(produitAdapter != null){
                produitAdapter.notifyDataSetChanged();
            }
        }
        produits = new ArrayList<>();
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
                CommandeActivity.this.finish();
                sessionManager.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void changerValidation(View view, int permission, TextView textValidation){
        LinearLayout parent1 = (LinearLayout) view.getParent().getParent();
        TextView nomProduit = (TextView) parent1.getChildAt(0);
        String SnomProduit = nomProduit.getText().toString();


        dansMehodeChangerValidation = true;



        Log.d("iddi", timeCommande);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("CommandesProduits").child(userName).child(timeCommande);
        Query query = reference.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            ElementCommande produit = dataSnapshot.getValue(ElementCommande.class);
                            if(produit.getNomProduit().equals(SnomProduit) && produit.getNomClient().equals(nomClient)){
                                if(permission == ACCEPTE) {
                                    dataSnapshot.getRef().child("validation").setValue("تم قبول المنتوج");
                                    textValidation.setText("تم قبول المنتوج");
                                }
                                else if(permission == REFUSE) {
                                    dataSnapshot.getRef().child("validation").setValue("تم رفض المنتوج");
                                    textValidation.setText("تم رفض المنتوج");
                                }
                                Log.d("iddi", produit.getNomProduit());
                            }
                        }
                        catch(Exception e){
                            Log.d("iddi", e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("iddi", "JJJJJJ");
    }

    public void envoyerReponse(View view){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("CommandesProduits").child(userName).child(timeCommande);
        Query query = reference.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            ElementCommande produitCommande = dataSnapshot.getValue(ElementCommande.class);
                            if(produitCommande.getNomClient().equals(nomClient)){
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("reponsesProduits").child(userName).child(timeCommande);
                                Reponse produitReponse = new Reponse(produitCommande.getNomClient(), produitCommande.getNomProduit(), produitCommande.getQuantiteProduit(), produitCommande.getCommentaireProduit(), produitCommande.getImage(), produitCommande.getPrix(), produitCommande.getValidation(), timeCommande);
                                reference.push().setValue(produitReponse);
                            }
                        }
                        catch(Exception e){
                            Log.d("iddi", e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference commandeReference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Commandes").child(userName).child(timeCommande);
        Query queryReference = commandeReference.orderByKey();
        queryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("nomClient").getValue().toString().equals(getIntent().getExtras().getString("nomClient"))){
                        dataSnapshot.getRef().child("situation").setValue(new Integer(TRAITE).toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onBackPressed();
    }
}