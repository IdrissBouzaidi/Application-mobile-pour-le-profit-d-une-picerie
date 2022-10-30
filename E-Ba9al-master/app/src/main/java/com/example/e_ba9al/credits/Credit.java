package com.example.e_ba9al.credits;

public class Credit {
    String nomClient, prixTotal;

    public Credit() {
    }

    public Credit(String nomClient, String prixTotal) {
        this.nomClient = nomClient;
        this.prixTotal = prixTotal;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getPrixTotal() {
        return prixTotal;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public void setPrixTotal(String prixTotal) {
        this.prixTotal = prixTotal;
    }
}
