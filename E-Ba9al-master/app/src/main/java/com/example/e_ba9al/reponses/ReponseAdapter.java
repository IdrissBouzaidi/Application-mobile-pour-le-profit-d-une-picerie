package com.example.e_ba9al.reponses;

import static com.example.e_ba9al.clients.NotificationsFragment.recyclerViewSupprimer;
import static com.example.e_ba9al.clients.NotificationsFragment.userNameEpicier;
import static com.example.e_ba9al.clients.NotificationsFragment.userName;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_ba9al.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReponseAdapter extends RecyclerView.Adapter<ReponseAdapter.ViewHolder> {
    public static ReponseAdapter reponsesAdapter;
    public static ArrayList reponses;

    public static class ViewHolder extends RecyclerView.ViewHolder{


        TextView nomProduit;
        TextView prixProduit;
        ImageView imageProduit;
        TextView quantiteProduit;
        TextView commentaireProduit;
        ImageView imageValidation;
        TextView textValidation;
        ImageButton supprimerProduit;
        TextView timeCommande;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            nomProduit = (TextView) itemView.findViewById(R.id.nomProduit);
            prixProduit = (TextView) itemView.findViewById(R.id.prixProduit);
            imageProduit = (ImageView) itemView.findViewById(R.id.imageProduit);
            quantiteProduit = (TextView) itemView.findViewById(R.id.quantiteProduit);
            commentaireProduit = (TextView) itemView.findViewById(R.id.commentaireProduit);
            imageValidation = itemView.findViewById(R.id.imageValidation);
            textValidation = itemView.findViewById(R.id.textValidation);
            supprimerProduit = itemView.findViewById(R.id.supprimerProduit);
            timeCommande = itemView.findViewById(R.id.timeCommande);
            supprimerProduit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Log.d("iddi", nomProduit.getText().toString());
                    String sNomProduit = nomProduit.getText().toString();
                    String sTimeProduit = timeCommande.getText().toString();






                    FirebaseApp.initializeApp(context.getApplicationContext());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
                    recyclerViewSupprimer.setLayoutManager(layoutManager);
                    recyclerViewSupprimer.setHasFixedSize(true);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("reponsesProduits").child(userNameEpicier);
                    reponses = new ArrayList<>();

                    //Clear ArrayList
                    ClearAll();
                    Query query = reference.orderByValue();
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ClearAll();
                            for(DataSnapshot snapshot1: dataSnapshot.getChildren()) {
                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                            Reponse produit = snapshot4.getValue(Reponse.class);
                                            try {
                                                if(produit.getNomClient().equals(userName)){
                                                    Log.d("iddi", sNomProduit);
                                                    if(produit.getTimeCommande().equals(sTimeProduit) && produit.getNomProduit().equals(sNomProduit)) {
                                                        Log.d("iddi", timeCommande.getText().toString());
                                                        Log.d("iddi", produit.getNomProduit());
                                                        snapshot4.getRef().removeValue();
                                                    }
                                                    else
                                                        reponses.add(produit);
                                                }
                                            }
                                            catch (Exception e){
                                                Log.d("iddi", e.getMessage());
                                            }
                                        }
                                    }
                                }
                            }
                            reponsesAdapter  = new ReponseAdapter(context, reponses);
                            recyclerViewSupprimer.setAdapter(reponsesAdapter);
                            reponsesAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });








                }
            });
        }
        public void ClearAll(){
            if(reponses != null){
                reponses.clear();
                if(reponsesAdapter != null){
                    reponsesAdapter.notifyDataSetChanged();
                }
            }
            reponses = new ArrayList<>();
        }
    }

    private static Context context;
    private List<Reponse> produits;
    final public static int ACCEPTE = 1;
    final public static int REFUSE = 0;
    public ReponseAdapter(Context c, List<Reponse> produitsList){
        this.context = c;
        this.produits = produitsList;
    }

    @NonNull
    @Override
    public ReponseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.element_notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReponseAdapter.ViewHolder holder, int position) {
        Reponse produit = produits.get(position);
        holder.nomProduit.setText(produit.getNomProduit());
        holder.prixProduit.setText("-" + produit.getPrix() + " درهم-");
        Glide.with(context).load(produit.getImage()).into(holder.imageProduit);
        holder.quantiteProduit.setText("-" + produit.getQuantiteProduit() + "-");
        holder.commentaireProduit.setText(produit.getCommentaireProduit());
        holder.timeCommande.setText(produit.getTimeCommande());
        if(produit.getValidation().equals("تم قبول المنتوج")) {
            Glide.with(context.getApplicationContext()).load(R.mipmap.accepter).into(holder.imageValidation);
            holder.textValidation.setTextColor(Color.parseColor("#A4C639"));
        }
        if(produit.getValidation().equals("تم رفض المنتوج")) {
            Glide.with(context.getApplicationContext()).load(R.mipmap.refuser).into(holder.imageValidation);
            holder.textValidation.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.textValidation.setText(produit.getValidation());
    }

    @Override
    public int getItemCount() {
        return produits.size();
    }
}
