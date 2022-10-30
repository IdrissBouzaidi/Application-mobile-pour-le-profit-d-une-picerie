package com.example.e_ba9al.credits;

import static com.example.e_ba9al.epiciers.CreditsFragment.insererLePrix;
import static com.example.e_ba9al.epiciers.CreditsFragment.supprimerClient;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_ba9al.R;

import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//Un élément de cette classe contient les éléments du View.
        TextView nomClient;
        TextView prixTatal;
        ImageButton ajouterPrix;
        ImageButton redtuirePrix;
        EditText prixCredit;
        static int prix;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomClient = (TextView) itemView.findViewById(R.id.nomClient);
            prixTatal = (TextView) itemView.findViewById(R.id.prixTatal);
            prixCredit = (EditText) itemView.findViewById(R.id.prixCredit);

            ajouterPrix = (ImageButton) itemView.findViewById(R.id.ajouterPrix);
            prixCredit = (EditText) itemView.findViewById(R.id.prixCredit);
            ajouterPrix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        prix = Integer.parseInt(prixCredit.getText().toString());
                        insererLePrix(nomClient.getText().toString(), prix, prixCredit);
                    }
                    catch (NumberFormatException e){
                        if(prixCredit.getText().toString().equals("")){
                            insererLePrix(nomClient.getText().toString(), 1, prixCredit);
                        }
                        else{
                            prixCredit.setText("");
                            prixCredit.setError("المرجو إدخال أرقام");
                            prixCredit.requestFocus();
                        }
                    }
                }
            });

            redtuirePrix = (ImageButton) itemView.findViewById(R.id.redtuirePrix);
            redtuirePrix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        prix = Integer.parseInt(prixCredit.getText().toString());
                        insererLePrix(nomClient.getText().toString(), -prix, prixCredit);
                    }
                    catch (NumberFormatException e){
                        if(prixCredit.getText().toString().equals("")){
                            insererLePrix(nomClient.getText().toString(), -1, prixCredit);
                        }
                        else{
                            prixCredit.setText("");
                            prixCredit.setError("المرجو إدخال أرقام");
                            prixCredit.requestFocus();
                        }
                    }
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
    private List<Credit> credits;
    static private RecyclerViewClickListener listener;
    public CreditAdapter(Context c, List<Credit> creditList, RecyclerViewClickListener listener){
        this.context = c;
        this.credits = creditList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.credit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Credit p = credits.get(position);
        holder.nomClient.setText(p.getNomClient());
        holder.prixTatal.setText(p.getPrixTotal() + " درهم");

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);
                dialogDelete.setTitle("تحذير!!");
                dialogDelete.setMessage("هل تريد حقا إزالة الزبون");
                dialogDelete.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        supprimerClient(p.getNomClient());
                        Toast.makeText(context,"تم حذف الزبون",Toast.LENGTH_SHORT).show();
                    }
                });
                dialogDelete.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogDelete.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return credits.size();
    }
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
}
