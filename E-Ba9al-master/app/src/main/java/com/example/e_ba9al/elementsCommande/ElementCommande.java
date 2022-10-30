package com.example.e_ba9al.elementsCommande;

public class ElementCommande {
    String nomClient, nomProduit, quantiteProduit, commentaireProduit;
    String image, prix, validation;

    public ElementCommande() {
    }

    public ElementCommande(String nomClient, String nomProduit, String quantiteProduit, String commentaireProduit, String image, String prix, String validation) {
        this.nomClient = nomClient;
        this.nomProduit = nomProduit;
        this.quantiteProduit = quantiteProduit;
        this.commentaireProduit = commentaireProduit;
        this.image = image;
        this.prix = prix;
        this.validation = validation;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public String getQuantiteProduit() {
        return quantiteProduit;
    }

    public String getCommentaireProduit() {
        return commentaireProduit;
    }

    public String getImage() {
        return image;
    }

    public String getPrix() {
        return prix;
    }

    public String getValidation() {
        return validation;
    }
}
