package com.example.e_ba9al.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {
    //instance of collections
    //public static CollectionReference ref_client;
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static final DatabaseReference clientRef = db.getReference("BD_E_BA9AL_Clients");


}
