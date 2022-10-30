package com.example.e_ba9al.epiciers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_ba9al.R;
import com.example.e_ba9al.commandes.Commande;
import com.example.e_ba9al.commandes.CommandeAdapter;
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


public class CommandesFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommandeAdapter commandesAdapter;
    private ArrayList<Commande> commandes;
    private Context context;
    public DatabaseReference reference;
    private CommandeAdapter.RecyclerViewClickListener listener;
    Toolbar toolbar;

    public static String userName;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commandes, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        //Récupérer l'email
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userName = user.get(sessionManager.USERNAME);
        Log.d("idid", userName);


        FirebaseApp.initializeApp(getContext());
        clickOniTem();
        recyclerView = view.findViewById(R.id.itemCommandeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //firebase
        reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Commandes").child(userName);
        commandes = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        //Firebase
        getData();
        return view;
    }

    private void clickOniTem() {
        listener = new CommandeAdapter.RecyclerViewClickListener(){

            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), CommandeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nomClient", commandes.get(position).getNomClient());
                bundle.putString("timeCommande", commandes.get(position).getTimeCommande());
                bundle.putString("situation", commandes.get(position).getSituation());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    public void getData(){
        Query query = reference.orderByValue();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("iddi", "nbre : " + commandes.size());
                ClearAll();
                for(DataSnapshot snapshot1: dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                            for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                Commande commande = snapshot4.getValue(Commande.class);
                                commandes.add(0, commande);
                            }
                        }
                    }
                }
                commandesAdapter  = new CommandeAdapter(getContext(), commandes, listener);
                recyclerView.setAdapter(commandesAdapter);
                commandesAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ClearAll(){
        if(commandes != null){
            commandes.clear();
            if(commandesAdapter != null){
                commandesAdapter.notifyDataSetChanged();
            }
        }
        commandes = new ArrayList<>();
    }
}