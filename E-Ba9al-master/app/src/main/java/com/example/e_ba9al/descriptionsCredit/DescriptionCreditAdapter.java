package com.example.e_ba9al.descriptionsCredit;

import static com.example.e_ba9al.epiciers.DescriptionsCreditActivity.supprimerDescriptionCredit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_ba9al.R;

import java.util.List;

public class DescriptionCreditAdapter extends RecyclerView.Adapter<DescriptionCreditAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView descriptionCredit;
        TextView tempsAjout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            descriptionCredit = (TextView) itemView.findViewById(R.id.descriptionCredit);
            tempsAjout = (TextView) itemView.findViewById(R.id.tempsAjout);
        }
    }

    private static Context context;
    private List<DescriptionCredit> descriptions;
    public DescriptionCreditAdapter(Context c, List<DescriptionCredit> descriptionsList){
        this.context = c;
        this.descriptions = descriptionsList;
    }

    @NonNull
    @Override
    public DescriptionCreditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.description_credit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionCreditAdapter.ViewHolder holder, int position) {
        DescriptionCredit descriptionCredit = descriptions.get(position);

        holder.descriptionCredit.setText(descriptionCredit.getDescription());
        holder.tempsAjout.setText(descriptionCredit.getTempsAjout());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenu().add("حذف");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("حذف")){
                            supprimerDescriptionCredit(descriptionCredit);

                            Toast.makeText(context,"تم حذف الملاحظة",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }
}
