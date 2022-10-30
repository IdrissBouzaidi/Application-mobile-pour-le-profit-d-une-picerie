package com.example.e_ba9al.elementsCommande;

import static com.example.e_ba9al.epiciers.CommandeActivity.changerValidation;
import static com.example.e_ba9al.epiciers.CommandeActivity.situation;
import static com.example.e_ba9al.commandes.Commande.TRAITE;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_ba9al.R;

import java.util.List;

public class ElementCommandeAdapter extends RecyclerView.Adapter<ElementCommandeAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nomProduit;
        TextView prixProduit;
        ImageView imageProduit;
        TextView quantiteProduit;
        TextView commentaireProduit;
        ImageView imageValidation;
        TextView textValidation;
        Button accepter;
        Button refuser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            nomProduit = (TextView) itemView.findViewById(R.id.nomProduit);
            prixProduit = (TextView) itemView.findViewById(R.id.prixProduit);
            imageProduit = (ImageView) itemView.findViewById(R.id.imageProduit);
            quantiteProduit = (TextView) itemView.findViewById(R.id.quantiteProduit);
            commentaireProduit = (TextView) itemView.findViewById(R.id.commentaireProduit);
            imageValidation = itemView.findViewById(R.id.imageValidation);
            textValidation = itemView.findViewById(R.id.textValidation);
            accepter = itemView.findViewById(R.id.accepter);
            refuser = itemView.findViewById(R.id.refuser);
            if(Integer.parseInt(situation) == TRAITE){
                accepter.setVisibility(View.INVISIBLE);
                refuser.setVisibility(View.INVISIBLE);
                LinearLayout linearAccepterRefuser = itemView.findViewById(R.id.linearAccepterRefuser);
                ViewGroup.LayoutParams params = linearAccepterRefuser.getLayoutParams();
                params.height = 0;
                params.width = 0;
                linearAccepterRefuser.setLayoutParams(params);
            }
            else{
                accepter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("iddi", "Adapter accepter");
                        changerValidation(view, ACCEPTE, textValidation);
                        Glide.with(context.getApplicationContext()).load(R.mipmap.accepter).into(imageValidation);
                        textValidation.setTextColor(Color.parseColor("#A4C639"));
                    }
                });
                refuser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("iddi", "Adapter refuser");
                        changerValidation(view, REFUSE, textValidation);
                        Glide.with(context.getApplicationContext()).load(R.mipmap.refuser).into(imageValidation);
                        textValidation.setTextColor(Color.parseColor("#FF0000"));
                    }
                });
            }
        }
    }

    private static Context context;
    private List<ElementCommande> produits;
    final public static int ACCEPTE = 1;
    final public static int REFUSE = 0;
    public ElementCommandeAdapter(Context c, List<ElementCommande> produitsList){
        this.context = c;
        this.produits = produitsList;
    }

    @NonNull
    @Override
    public ElementCommandeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.element_commande, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ElementCommandeAdapter.ViewHolder holder, int position) {
        ElementCommande commande = produits.get(position);

        holder.nomProduit.setText(commande.getNomProduit());
        holder.prixProduit.setText("-" + commande.getPrix() + " درهم-");
        Glide.with(context).load(commande.getImage()).into(holder.imageProduit);
        holder.quantiteProduit.setText("-" + commande.getQuantiteProduit() + "-");
        holder.commentaireProduit.setText(commande.getCommentaireProduit());

        if(Integer.parseInt(situation) == TRAITE){
            if(commande.getValidation().equals("تم قبول المنتوج")){
                Glide.with(context.getApplicationContext()).load(R.mipmap.accepter).into(holder.imageValidation);
                holder.textValidation.setTextColor(Color.parseColor("#A4C639"));
                holder.textValidation.setText("تم قبول المنتوج");
            }
            else{
                Glide.with(context.getApplicationContext()).load(R.mipmap.refuser).into(holder.imageValidation);
                holder.textValidation.setTextColor(Color.parseColor("#FF0000"));
                holder.textValidation.setText("تم رفض المنتوج");
            }
        }

    }

    @Override
    public int getItemCount() {
        return produits.size();
    }
}
