package com.example.e_ba9al.articles;

import static com.example.e_ba9al.clients.ArticlesFragment.listeArticles2;
import static java.lang.Integer.parseInt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.e_ba9al.R;

public class ArticlesAdapter extends ArrayAdapter {
    Context context;
    int adapterResource;
    Article[] listeArticles;
    public ArticlesAdapter(@NonNull Context context, int resource, @NonNull Article[] listeArticles) {
        super(context, resource, listeArticles);
        this.context = context;
        adapterResource = resource;
        this.listeArticles = listeArticles;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int p = position;
        View view;
        LayoutInflater viewInflater = LayoutInflater.from(context);
        view = viewInflater.inflate(adapterResource, parent, false);

        TextView nomArticle = view.findViewById(R.id.nomArticle);
        ImageView imageArticle = view.findViewById(R.id.imageArticle);
        CardView cardViewArticle = view.findViewById(R.id.cardViewArticle);

        Article article = listeArticles[position];


        nomArticle.setText(article.getNomArticle());
        Glide.with(getContext()).load(article.getImageArticle()).into(imageArticle);
        if(listeArticles == listeArticles2){
            p++;
        }
        if(p%2 == 0)
            cardViewArticle.setBackgroundResource(R.drawable.gradientbackground);
        else
            cardViewArticle.setBackgroundResource(R.drawable.gradientbackground2);
        return view;
    }
}
