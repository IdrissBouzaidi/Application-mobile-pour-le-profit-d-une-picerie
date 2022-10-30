package com.example.e_ba9al.clients;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.e_ba9al.R;
import com.example.e_ba9al.articles.Article;
import com.example.e_ba9al.articles.ArticlesAdapter;

public class ArticlesFragment extends Fragment {
    String categorie;
    ListView listeViewArticles1;
    ListView listeViewArticles2;
    public static Article[] listeArticles1 = new Article[3];
    public static Article[] listeArticles2 = new Article[3];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);


        listeViewArticles1 = view.findViewById(R.id.listeViewArticles1);
        listeViewArticles2 = view.findViewById(R.id.listeViewArticles2);
        Article boisson = new Article("مشروبات", R.drawable.boisson);
        Article graines = new Article("قطنيات", R.drawable.graines);
        Article laits = new Article("حليب و مشتقاته", R.drawable.laits);
        Article hygiene = new Article("مواد التنظيف", R.drawable.hygiene);
        Article cookie = new Article("حلويات", R.drawable.cookie);
        Article autres = new Article("منتوجات اخرى", R.drawable.autres);
        listeArticles1[0] = boisson;
        listeArticles1[1] = graines;
        listeArticles1[2] = laits;
        listeArticles2[0] = hygiene;
        listeArticles2[1] = cookie;
        listeArticles2[2] = autres;

        int xmlFile = R.layout.article;
        ArticlesAdapter articlesAdapter1 = new ArticlesAdapter(getContext(), xmlFile, listeArticles1);
        ArticlesAdapter articlesAdapter2 = new ArticlesAdapter(getContext(), xmlFile, listeArticles2);
        listeViewArticles1.setAdapter(articlesAdapter1);
        listeViewArticles1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        categorie = "boisson";
                        break;
                    case 1:
                        categorie = "graines";
                        break;
                    case 2:
                        categorie = "laits";
                        break;
                }
                actionClickItemListView(position);
            }
        });
        listeViewArticles2.setAdapter(articlesAdapter2);
        listeViewArticles2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        categorie = "hygiene";
                        break;
                    case 1:
                        categorie = "cookies";
                        break;
                    case 2:
                        categorie = "autres";
                        break;
                }
                actionClickItemListView(position);
            }
        });
        return view;
    }
    public void actionClickItemListView(int position){
        try{
            Intent intent = new Intent(getActivity().getBaseContext(), ProduitsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("nomCategorie", categorie);
            intent.putExtras(bundle);
            Log.d("iddi", "HHHHHH");
            getActivity().startActivity(intent);
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }
    }
}