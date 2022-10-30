package com.example.e_ba9al.panier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_ba9al.R;

import java.util.List;

public class PanierAdapter extends RecyclerView.Adapter<com.example.e_ba9al.panier.PanierAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nom;
        ImageView imageProduit;
        TextView prixProduit;
        TextView quantiteProduit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.nomProduit);
            imageProduit = (ImageView) itemView.findViewById(R.id.imageProduit);
            prixProduit = (TextView) itemView.findViewById(R.id.prixProduit);
            quantiteProduit = (TextView) itemView.findViewById(R.id.quantiteProduit);
        }
    }

    private Context context;
    private List<Panier> produits;
    public PanierAdapter(Context c, List<Panier> produitsList){
        this.context = c;
        this.produits = produitsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.element_panier, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PanierAdapter.ViewHolder holder, int position) {
        Panier p = produits.get(position);
        holder.nom.setText("-" + p.nomProduit + "-");
        Glide.with(context).load(produits.get(position).image).into(holder.imageProduit);
        holder.prixProduit.setText("-" + p.getPrix() + " درهم-");
        holder.quantiteProduit.setText(p.getQuantiteProduit());
    }

    @Override
    public int getItemCount() {
        return produits.size();
    }
}
