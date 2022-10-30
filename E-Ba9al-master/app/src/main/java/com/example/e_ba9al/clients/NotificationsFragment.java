package com.example.e_ba9al.clients;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_ba9al.R;
import com.example.e_ba9al.reponses.Reponse;
import com.example.e_ba9al.reponses.ReponseAdapter;
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

public class NotificationsFragment extends Fragment {
    public static String userName;
    public static String userNameEpicier;
    SessionManager sessionManager;


    private RecyclerView recyclerView;
    public static RecyclerView recyclerViewSupprimer;
    private ReponseAdapter reponsesAdapter;
    private ArrayList<Reponse> reponses;
    private Context context;
    public DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        //Récupérer l'email
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userName = user.get(sessionManager.USERNAME);
        userNameEpicier = user.get(sessionManager.USERNAMEEPICIER);
        try {
            recyclerViewSupprimer = view.findViewById(R.id.itemReponses);

            FirebaseApp.initializeApp(getContext());
            recyclerView = view.findViewById(R.id.itemReponses);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            //firebase
            reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("reponsesProduits").child(userNameEpicier);
            reponses = new ArrayList<>();

            //Clear ArrayList
            ClearAll1();

            //Firebase
            getData();
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }
        return view;


    }

    private void getData() {

        Query query = reference.orderByValue();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ClearAll1();
                    for(DataSnapshot snapshot1: dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                    Reponse produit = snapshot4.getValue(Reponse.class);
                                    try {
                                        if(produit.getNomClient().equals(userName))
                                            reponses.add(produit);
                                    }
                                    catch (Exception e){
                                        Log.d("iddi", e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                    reponsesAdapter  = new ReponseAdapter(getContext(), reponses);
                    recyclerView.setAdapter(reponsesAdapter);
                    reponsesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ClearAll1(){
        if(reponses != null){
            reponses.clear();
            if(reponsesAdapter != null){
                reponsesAdapter.notifyDataSetChanged();
            }
        }
        reponses = new ArrayList<>();
    }
}