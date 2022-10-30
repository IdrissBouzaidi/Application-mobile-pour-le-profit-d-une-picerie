package com.example.e_ba9al.produits;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_ba9al.clients.AccueilClientActivity;
import com.example.e_ba9al.clients.AjouterAuPanierActivity;
import com.example.e_ba9al.clients.ProduitsActivity;
import com.example.e_ba9al.R;

import java.util.List;

public class ProduitAccueilAdapter extends RecyclerView.Adapter<ProduitAccueilAdapter.ViewHolder> {









    public static class ViewHolder extends RecyclerView.ViewHolder{//Un élément de cette classe contient les éléments du View.
        TextView nom;
        ImageView image;
        TextView quantite;
        TextView prix;
        LinearLayout numero;//Le numéro du produit dans la liste des produits, 2 produits ne diuvent pas avoir le même numéro.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.nomProduit);
            image = (ImageView) itemView.findViewById(R.id.imageProduit);
            quantite = (TextView) itemView.findViewById(R.id.quantiteProduit);
            prix = (TextView) itemView.findViewById(R.id.prixProduit);
            numero = (LinearLayout) itemView.findViewById(R.id.produitNo);
            itemView.findViewById(R.id.passerALaPageAjouterAuPanier).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(context.getApplicationContext(), AjouterAuPanierActivity.class);
                        Bundle bundle = new Bundle();
                        Log.d("iddi", "haha");
                        bundle.putString("nomProduit", nom.getText().toString());

                        String myCategorie = null;
                        if(ProduitsActivity.categorie != null) {
                            Log.d("iddi", "categorie est dans listProduits");
                            myCategorie = ProduitsActivity.categorie;
                            bundle.putString("nomCategorie", myCategorie);

                            bundle.putString("pagePrecedente", "ListProduits");
                            intent.putExtras(bundle);
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.getApplicationContext().startActivity(intent);


                        }
                        else{

                            Log.d("iddi", "categorie est dans Accueil");
                            AccueilClientActivity.passerALaPageAjouterAuPanier(view, context, intent, bundle, nom);
                            Log.d("iddi", "la categorie : " + myCategorie);
                        }
                }
            });
        }
    }

    static private Context context;
    private List<Produit> produits;
    public ProduitAccueilAdapter(Context c, List<Produit> produitList){
        this.context = c;
        this.produits = produitList;
    }

    @NonNull
    @Override
    public ProduitAccueilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_produit_accueil, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Produit p = produits.get(position);
        holder.nom.setText("-" + p.nom + "-");
        Glide.with(context).load(produits.get(position).image).into(holder.image);
        holder.prix.setText("-" + p.prix + " درهم-");





    }

    @Override
    public int getItemCount() {
        return produits.size();
    }





















}
