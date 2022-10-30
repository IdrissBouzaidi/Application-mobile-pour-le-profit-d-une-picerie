package com.example.e_ba9al.produits;

public class Produit {
    String nom, image, prix;

    public Produit() {
    }

    public Produit(String nom, String image, String prix) {
        this.nom = nom;
        this.image = image;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public String getImage() {
        return image;
    }

    public String getPrix() {
        return prix;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}
