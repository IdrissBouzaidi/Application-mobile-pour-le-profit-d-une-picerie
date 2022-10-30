package com.example.e_ba9al.epiciers;

import static com.example.e_ba9al.epiciers.AccueilEpicierActivity.userName;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_ba9al.R;
import com.example.e_ba9al.credits.Credit;
import com.example.e_ba9al.credits.CreditAdapter;
import com.example.e_ba9al.session.SessionManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class CreditsFragment extends Fragment {
    private static RecyclerView recyclerView;
    private static CreditAdapter creditAdapter;
    private ArrayList<Credit> credits;
    private static Context context;
    public static DatabaseReference reference;
    static boolean estClient;
    private CreditAdapter.RecyclerViewClickListener listener;
    Toolbar toolbar;

    static boolean voirToutClient;
    static boolean apresAjouerAuCarnet;


    EditText rechercheClient;
    AppCompatButton ajouterAuCarnet;
    EditText prixTotal;
    EditText nomClient;
    ImageButton voirTousLesClients;

    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_credits, container, false);

        context = getContext();

        toolbar = view.findViewById(R.id.toolbar);
        //Récupérer l'email
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();

        rechercheClient = view.findViewById(R.id.rechercheClient);
        ajouterAuCarnet = view.findViewById(R.id.ajouterAuCarnet);
        prixTotal = view.findViewById(R.id.prixTotal);
        nomClient = view.findViewById(R.id.nomClient);
        voirTousLesClients = view.findViewById(R.id.voirTousLesClients);

        FirebaseApp.initializeApp(getContext());
        clickOniTem();
        recyclerView = view.findViewById(R.id.itemCreditRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //firebase
        reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Credit").child(userName);
        credits = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        //Firebase
        getData();
        rechercheClient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                estClient = false;
                getData();
            }
        });

        apresAjouerAuCarnet = false;
        ajouterAuCarnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = reference.orderByValue();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        estClient = false;
                        if(!nomClient.getText().toString().equals("")) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Credit credit = dataSnapshot.getValue(Credit.class);
                                Log.d("iddi", "credit, nom client : " + credit.getNomClient());
                                Log.d("iddi", "nomClient : " + nomClient);
                                if (credit.getNomClient().equals(nomClient.getText().toString())) {
                                    Toast.makeText(getContext(), "الزبون موجود مسبقا", Toast.LENGTH_LONG).show();
                                    estClient = true;
                                    getData();
                                    break;
                                }
                            }
                            if(estClient == false){
                                Credit credit = new Credit();
                                credit.setNomClient(nomClient.getText().toString());
                                if(prixTotal.getText().toString().equals("")){
                                    credit.setPrixTotal("0");
                                }
                                else{
                                    try{
                                        int prixInitial = Integer.parseInt(prixTotal.getText().toString());
                                        credit.setPrixTotal(new Integer(prixInitial).toString());
                                    }
                                    catch (NumberFormatException e){
                                        credit.setPrixTotal("");
                                        prixTotal.setText("");
                                        prixTotal.setError("المرجو إدخال أرقام");
                                        prixTotal.requestFocus();
                                    }
                                }
                                if(!credit.getPrixTotal().equals(""))
                                    reference.push().setValue(credit);
                                nomClient.setText("");
                                prixTotal.setText("");
                            }
                            else{
                                apresAjouerAuCarnet = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        voirToutClient = false;
        voirTousLesClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(voirToutClient == false)
                    Glide.with(context).load(R.drawable.eye_green).into(voirTousLesClients);
                else
                Glide.with(context).load(R.drawable.eye_black).into(voirTousLesClients);
                voirToutClient = !voirToutClient;
                getData();
            }
        });


        nomClient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(apresAjouerAuCarnet == true)
                    getData();
                apresAjouerAuCarnet = false;
            }
        });
        return view;
    }

    private void clickOniTem() {
        listener = new CreditAdapter.RecyclerViewClickListener(){

            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, DescriptionsCreditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nomClient", credits.get(position).getNomClient());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    public void getData(){
        Query query = reference.orderByValue();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("iddi", "est client : " + estClient);
                ClearAll();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Credit credit = snapshot.getValue(Credit.class);
                    if(voirToutClient == true){//L'oeuil est activée
                        if(estClient == true){//Dans le cas d'un nom d'utilisateur sur lequel on cherche et on trouve déja avec les clients
                            if(credit.getNomClient().equals(nomClient.getText().toString()))
                                credits.add(credit);
                        }
                        else if (credit.getNomClient().contains(rechercheClient.getText()))//si la recherche est vide, alors on va tout voir, sinon alors on va voir que ce qu'on voudrait
                            credits.add(credit);
                    }
                    else{//L'oeuil n'est pas activée
                        if(estClient == true){//Dans le cas d'un nom d'utilisateur sur lequel on cherche et on trouve déja avec les clients
                            if(credit.getNomClient().equals(nomClient.getText().toString()))
                                credits.add(credit);
                        }
                        else if (credit.getNomClient().contains(rechercheClient.getText()) && Double.parseDouble(credit.getPrixTotal()) != 0)//si la recherche est vide, alors on va tout voir, sinon alors on va voir que ce qu'on voudrait
                            credits.add(credit);
                    }
                }
                estClient = false;

                creditAdapter  = new CreditAdapter(getContext(), credits, listener);
                recyclerView.setAdapter(creditAdapter);
                creditAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void ClearAll(){
        if(credits != null){
            credits.clear();
            if(creditAdapter != null){
                creditAdapter.notifyDataSetChanged();
            }
        }
        credits = new ArrayList<>();
    }
    public static void insererLePrix(String nomClient, int prix, EditText prixCredit){
        Query query = reference.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Credit credit = dataSnapshot.getValue(Credit.class);
                    Log.d("iddi", credit.getNomClient());
                    if(credit.getNomClient().equals(nomClient)) {
                        int prixTotal = prix + Integer.parseInt(credit.getPrixTotal());
                        if(prixTotal >= 0)
                            dataSnapshot.getRef().child("prixTotal").setValue(new Integer(prixTotal).toString());
                        else {
                            prixCredit.setError("المرجو إدخال رقم أصغر من الثمن الاجمالي");
                            prixCredit.requestFocus();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void supprimerClient(String nomClient){
        Query query = reference.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.d("iddi", dataSnapshot.child("nomClient").toString());
                    if(dataSnapshot.child("nomClient").getValue().toString().equals(nomClient)){
                        Log.d("iddi", nomClient);
                        dataSnapshot.getRef().removeValue();
                    }
                }
                recyclerView.setAdapter(creditAdapter);
                creditAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}