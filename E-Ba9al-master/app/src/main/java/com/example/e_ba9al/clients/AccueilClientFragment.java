package com.example.e_ba9al.clients;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_ba9al.R;
import com.example.e_ba9al.produits.Produit;
import com.example.e_ba9al.produits.ProduitAccueilAdapter;
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

public class AccueilClientFragment extends Fragment {
    static RecyclerView recyclerView1;
    static RecyclerView recyclerView2;
    static ProduitAccueilAdapter produitAccueilAdapter1;
    static ProduitAccueilAdapter produitAccueilAdapter2;
    private static ArrayList<Produit> produits1;
    private static ArrayList<Produit> produits2;
    public DatabaseReference reference;

    public static Context context;

    public static String userName;
    SessionManager sessionManager;

    public static View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accueil_client, container, false);

        context = getContext();

        ProduitsActivity.categorie = null;
        //Récupérer l'email
        sessionManager = new SessionManager(context);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userName = user.get(sessionManager.USERNAME);
        getDataFavoris(view);
        getDataRecommandes(view);
        return view;
    }
    public static void getDataFavoris(View view){
        try{

            FirebaseApp.initializeApp(context);
            recyclerView1 = view.findViewById(R.id.itemFavoris);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView1.setLayoutManager(linearLayoutManager);
            recyclerView1.setHasFixedSize(true);

            //firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Categorie");
            Query query = reference.orderByValue();
            produits1 = new ArrayList<>();

            //Clear ArrayList
            ClearAll1();


            DatabaseReference referenceFavoris = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Favoris").child(userName);
            Query query2 = referenceFavoris.orderByValue();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{

                        ClearAll1();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                Produit produit = snapshot2.getValue(Produit.class);
                                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try{
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                try{
                                                    if(dataSnapshot.getValue().equals(produit.getNom())) {
                                                        try{
                                                            produits1.add(produit);
                                                        }
                                                        catch (Exception e){
                                                            Log.d("iddi", e.getMessage());
                                                        }
                                                    }
                                                }
                                                catch (Exception e){
                                                    Log.d("iddi", e.getMessage());
                                                }
                                            }
                                            produitAccueilAdapter1.notifyDataSetChanged();
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
                        }
                        produitAccueilAdapter1 = new ProduitAccueilAdapter(context, produits1);
                        recyclerView1.setAdapter(produitAccueilAdapter1);
                        produitAccueilAdapter1.notifyDataSetChanged();
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
    public void getDataRecommandes(View view){
        FirebaseApp.initializeApp(getContext());
        recyclerView2 = view.findViewById(R.id.itemRecommandes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setHasFixedSize(true);

        //firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Recommandés");
        Query query = reference.orderByValue();
        ArrayList<Produit> produits = new ArrayList<>();

        //Clear ArrayList
        ClearAll2();


        Log.d("iddi", "hhhhhh");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll2();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("iddi", "hhh");
                    Produit produit = snapshot.getValue(Produit.class);
                    produits.add(produit);

                }
                produitAccueilAdapter2  = new ProduitAccueilAdapter(getContext(), produits);
                recyclerView2.setAdapter(produitAccueilAdapter2);
                produitAccueilAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void ClearAll1(){
        if(produits1 != null){
            produits1.clear();
            if(produitAccueilAdapter1 != null){
                produitAccueilAdapter1.notifyDataSetChanged();
            }
        }
        produits1 = new ArrayList<>();
    }
    public static void ClearAll2(){
        if(produits2 != null){
            produits2.clear();
            if(produitAccueilAdapter2 != null){
                produitAccueilAdapter2.notifyDataSetChanged();
            }
        }
        produits2 = new ArrayList<>();
    }
}