package com.example.e_ba9al.articles;

public class Article {
    private String nomArticle;
    private int imageArticle;

    public Article() {
    }

    public Article(String nomArticle, int imageArticle) {
        this.nomArticle = nomArticle;
        this.imageArticle = imageArticle;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public int getImageArticle() {
        return imageArticle;
    }
}
