package com.example.e_ba9al.panier;

public class Panier {
    String nomProduit, quantiteProduit, commentaireProduit;
    String image, prix;

    public Panier() {
    }

    public Panier(String nomProduit, String quantiteProduit, String commentaireProduit, String image, String prix) {
        this.nomProduit = nomProduit;
        this.quantiteProduit = quantiteProduit;
        this.commentaireProduit = commentaireProduit;
        this.image = image;
        this.prix = prix;
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

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setQuantiteProduit(String quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public void setCommentaireProduit(String commentaireProduit) {
        this.commentaireProduit = commentaireProduit;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}
