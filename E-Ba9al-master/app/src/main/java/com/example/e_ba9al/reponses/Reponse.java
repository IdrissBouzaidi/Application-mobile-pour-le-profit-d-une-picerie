package com.example.e_ba9al.reponses;

public class Reponse {
    String nomClient, nomProduit, quantiteProduit, commentaireProduit;
    String image, prix, validation, timeCommande;

    public Reponse() {
    }

    public Reponse(String nomClient, String nomProduit, String quantiteProduit, String commentaireProduit, String image, String prix, String validation, String timeCommande) {
        this.nomClient = nomClient;
        this.nomProduit = nomProduit;
        this.quantiteProduit = quantiteProduit;
        this.commentaireProduit = commentaireProduit;
        this.image = image;
        this.prix = prix;
        this.validation = validation;
        this.timeCommande = timeCommande;
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

    public String getTimeCommande() {
        return timeCommande;
    }
}
