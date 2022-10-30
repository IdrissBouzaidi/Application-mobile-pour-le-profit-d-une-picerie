package com.example.e_ba9al.epiciers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.e_ba9al.R;
import com.example.e_ba9al.descriptionsCredit.DescriptionCredit;
import com.example.e_ba9al.descriptionsCredit.DescriptionCreditAdapter;
import com.example.e_ba9al.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DescriptionsCreditActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static DescriptionCreditAdapter descriptionCreditAdapter;
    private static ArrayList<DescriptionCredit> descriptions;
    private static Context context;
    public static DatabaseReference reference;
    Toolbar toolbar;

    static EditText description;

    public static String userName;
    SessionManager sessionManager;

    static String nomClient;

    public static boolean dansMehodeChangerValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptions_credit);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Récupérer l'email
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userName = user.get(sessionManager.USERNAME);
        Log.d("idid", userName);

        Bundle bundle = getIntent().getExtras();

        nomClient = bundle.getString("nomClient");
        Log.d("iddi", nomClient);

        context = getApplicationContext();

        FirebaseApp.initializeApp(this);
        //clickOniTem();
        recyclerView = findViewById(R.id.descriptionsCreditRecyclerView);



        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        //recyclerView.setLayoutManager(mLayoutManager);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);



        recyclerView.setHasFixedSize(true);
        //firebase
        reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Descriptionscredits").child(userName).child(nomClient);
        descriptions = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        //Firebase
        getData();
    }

    public static void getData(){
        Query query = reference.orderByValue();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DescriptionCredit descriptionCredit = snapshot.getValue(DescriptionCredit.class);
                    descriptions.add(0, descriptionCredit);
                }
                descriptionCreditAdapter  = new DescriptionCreditAdapter(context, descriptions);
                recyclerView.setAdapter(descriptionCreditAdapter);
                descriptionCreditAdapter.notifyDataSetChanged();
                descriptionCreditAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void ClearAll(){
        if(descriptions != null){
            descriptions.clear();
            if(descriptionCreditAdapter != null){
                descriptionCreditAdapter.notifyDataSetChanged();
            }
        }
        descriptions = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_epicier, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        switch(idItem){
            case R.id.itemLogout:
                DescriptionsCreditActivity.this.finish();
                sessionManager.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ajouterDescriptionCredit(View view) {
        final Dialog dialog = new Dialog(DescriptionsCreditActivity.this);
        dialog.setContentView(R.layout.dialogue_ajouter_description);
        int width = (int) (DescriptionsCreditActivity.this.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (DescriptionsCreditActivity.this.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
        description = dialog.findViewById(R.id.description);
        MaterialButton savebtn = dialog.findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeNow;
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                timeNow = format.format(date);

                DescriptionCredit descriptionCredit = new DescriptionCredit(description.getText().toString(), timeNow);
                reference.push().setValue(descriptionCredit);
                dialog.dismiss();
                getData();
            }
        });
    }
    public static void supprimerDescriptionCredit(DescriptionCredit descriptionCredit){

        reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Descriptionscredits").child(userName).child(nomClient);
        Query query = reference.orderByValue();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("description").getValue().toString().equals(descriptionCredit.getDescription()) && snapshot.child("tempsAjout").getValue().toString().equals(descriptionCredit.getTempsAjout()))
                        snapshot.getRef().removeValue();
                }
                getData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}