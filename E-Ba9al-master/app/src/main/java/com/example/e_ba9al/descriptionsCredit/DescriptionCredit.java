package com.example.e_ba9al.descriptionsCredit;

public class DescriptionCredit {
    String description, tempsAjout;

    public DescriptionCredit() {
    }

    public DescriptionCredit(String description, String tempsAjout) {
        this.description = description;
        this.tempsAjout = tempsAjout;
    }

    public String getDescription() {
        return description;
    }

    public String getTempsAjout() {
        return tempsAjout;
    }
}
