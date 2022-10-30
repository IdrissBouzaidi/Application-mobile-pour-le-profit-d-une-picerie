package com.example.e_ba9al.commandes;

import static com.example.e_ba9al.epiciers.AccueilEpicierActivity.userName;
import static com.example.e_ba9al.commandes.Commande.TRAITE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_ba9al.R;
import com.example.e_ba9al.elementsCommande.ElementCommande;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//Un élément de cette classe contient les éléments du View.
        TextView nomClient;
        TextView nbreProduits;
        TextView prixTotal;
        TextView timeCommande;
        TextView situation;//Le numéro du produit dans la liste des produits, 2 produits ne diuvent pas avoir le même numéro.
        ImageButton supprimerProduit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomClient = (TextView) itemView.findViewById(R.id.nomClient);
            nbreProduits = (TextView) itemView.findViewById(R.id.nbreProduits);
            prixTotal = (TextView) itemView.findViewById(R.id.prixTotal);
            timeCommande = (TextView) itemView.findViewById(R.id.timeCommande);
            situation = (TextView) itemView.findViewById(R.id.situation);
            supprimerProduit = itemView.findViewById(R.id.supprimerProduit);
            supprimerProduit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("iddi", userName);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("CommandesProduits").child(userName).child(timeCommande.getText().toString());
                    Query query = reference.orderByValue();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ElementCommande produitCommande = dataSnapshot.getValue(ElementCommande.class);
                                    if(produitCommande.getNomClient().equals(nomClient.getText().toString()))
                                        dataSnapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference referenceCommande = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Commandes").child(userName).child(timeCommande.getText().toString());
                    Query queryCommande = referenceCommande.orderByValue();
                    queryCommande.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Commande commande = dataSnapshot.getValue(Commande.class);
                                    if(commande.getNomClient().equals(nomClient.getText().toString()))
                                        dataSnapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    private Context context;
    private List<Commande> commandes;
    static private RecyclerViewClickListener listener;
    public CommandeAdapter(Context c, List<Commande> commandeList, RecyclerViewClickListener listener){
        this.context = c;
        this.commandes = commandeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommandeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.commande, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Commande p = commandes.get(position);
        holder.nomClient.setText(p.nomClient);
        holder.nbreProduits.setText("-" + p.nbreProduits + "-");
        holder.prixTotal.setText("-" + p.prixTotal + " درهم-");
        holder.timeCommande.setText(p.timeCommande);
        if(Integer.parseInt(p.getSituation()) == TRAITE) {
            holder.situation.setText("معالج");
            holder.situation.setTextColor(Color.parseColor("#A4C639"));
        }
        else {
            holder.situation.setText("غير معالج");
            holder.situation.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public int getItemCount() {
        return commandes.size();
    }
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
}
