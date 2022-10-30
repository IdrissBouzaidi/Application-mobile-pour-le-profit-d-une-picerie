package com.example.e_ba9al.commandes;

public class Commande {
    String nomClient, timeCommande, prixTotal, nbreProduits, situation;
    static final public int PAS_TRAITE = 0;//Le produit n'est pas encore traité.
    static final public int TRAITE = 1;//Le produit est déja traité.

    public Commande() {
    }

    public Commande(String nomClient, String timeCommande, String prixTotal, String nbreProduits, String situation) {
        this.nomClient = nomClient;
        this.timeCommande = timeCommande;
        this.prixTotal = prixTotal;
        this.nbreProduits = nbreProduits;
        this.situation = situation;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getTimeCommande() {
        return timeCommande;
    }

    public String getPrixTotal() {
        return prixTotal;
    }

    public String getNbreProduits() {
        return nbreProduits;
    }

    public String getSituation() {
        return situation;
    }
}
