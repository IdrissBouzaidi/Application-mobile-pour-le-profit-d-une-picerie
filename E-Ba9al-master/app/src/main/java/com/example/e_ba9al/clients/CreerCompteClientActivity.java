package com.example.e_ba9al.clients;

import static com.example.e_ba9al.config.Firebase.clientRef;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_ba9al.Assist.Help;
import com.example.e_ba9al.clients_epiciers.AuthentificationActivity;
import com.example.e_ba9al.R;
import com.example.e_ba9al.config.Firebase;
import com.example.e_ba9al.credits.Credit;
import com.example.e_ba9al.session.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CreerCompteClientActivity extends AppCompatActivity {

    Button registerBtn;
    EditText clientUsernameInput, clientPhoneInput, passwordInput , epecierPhoneInput ;
    TextView out;
    FirebaseDatabase rootNode;
    DatabaseReference reference ;
    FirebaseAuth mauth;
    ProgressDialog loadingBar;
    static boolean inActivity;

    Intent intent;

    static String epicierPhoneNumber;
    static String userNameEpicier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_compte_client);
        SessionManager sessionManager = new SessionManager(this);

        inActivity = true;

        // make the keyboard hidden
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        clientUsernameInput = (EditText) findViewById(R.id.UserNameClient);
        clientPhoneInput = (EditText) findViewById(R.id.phoneClient);
        passwordInput =  (EditText) findViewById(R.id.password);
        epecierPhoneInput = (EditText) findViewById(R.id.epicierPhone);
        registerBtn = (Button) findViewById(R.id.makeAccount);
        out = (TextView) findViewById(R.id.haveYouAlrAcc);
        mauth = FirebaseAuth.getInstance();
        ProgressDialog loadingBar = new ProgressDialog(this);
        // action after registerBtn
        registerBtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                String _userNameInput = clientUsernameInput.getText().toString().trim();
                String _clientPhoneInput = clientPhoneInput.getText().toString().trim();
                String _passwordInput = passwordInput.getText().toString().trim();
                String _epecierPhoneInput= epecierPhoneInput.getText().toString().trim();

                 validateData(_userNameInput,_clientPhoneInput,_passwordInput,_epecierPhoneInput);
            }

            private boolean validateData(String _userNameInput,String _clientPhoneInput,String _passwordInput,String _epecierPhoneInput){

                // check the data
               if(true){
                   if (TextUtils.isEmpty(_userNameInput))
                   {
                       //Toast.makeText(RegistreClient.this, "المرجو إدخال إسمكم بشكل صحيح", Toast.LENGTH_SHORT).show();
                       clientUsernameInput.setError("المرجو إدخال إسمكم ");
                       clientUsernameInput.requestFocus();
                   }
                   else if (TextUtils.isEmpty(_clientPhoneInput))
                   {
                       //Toast.makeText(RegistreClient.this, "المرجو إدخال رقم هاتفكم بشكل صحيح", Toast.LENGTH_SHORT).show();
                       clientPhoneInput.setError("المرجو إدخال رقم هاتفكم ");
                       clientPhoneInput.requestFocus();
                   }
                   else if (TextUtils.isEmpty(_passwordInput))
                   {
                       // Toast.makeText(RegistreClient.this, "المرجو إختيار كلمة مروركم بشكل صحيح", Toast.LENGTH_SHORT).show();
                       passwordInput.setError("المرجو دخال كلمة مروركم ");
                       passwordInput.requestFocus();
                   }
                   else if (TextUtils.isEmpty(_epecierPhoneInput))
                   {
                       //Toast.makeText(RegistreClient.this, "المرجو إدخال كلمة رقم هاتف بفالكم بشكل صحيح", Toast.LENGTH_SHORT).show();
                       epecierPhoneInput.setError("المرجو إدخال كلمة رقم هاتف بفالكم ");
                       epecierPhoneInput.requestFocus();
                   }


                   // ****
                   else if(!Help.isValidUserName(_userNameInput)){
                       clientUsernameInput.setError("المرجو إدخال إسمكم بشكل صحيح");
                       clientUsernameInput.requestFocus();
                   }
                   else if(!Help.isValidPhoneNum(_clientPhoneInput)){
                       clientPhoneInput.setError("المرجو إدخال رقم هاتفكم بشكل صحيح");
                       clientPhoneInput.requestFocus();
                   }
                   else if(!Help.isValidPassword(_passwordInput)){
                       passwordInput.setError("المرجو إختيار كلمة مروركم بشكل صحيح ( إختر على الأقل 6 حروف أو أرقام ) ");
                       passwordInput.requestFocus();

                   }else if(!Help.isValidPhoneNum(_epecierPhoneInput)){
                       epecierPhoneInput.setError("المرجو إدخال كلمة رقم هاتف بفالكم بشكل صحيح");
                       epecierPhoneInput.requestFocus();
                   }
                   else{
                       loadingBar.setTitle("إنشاء حساب");
                       loadingBar.setMessage("المرجو الإنتظار قليلا حتى نتحقق من المعلومات");
                       loadingBar.setCanceledOnTouchOutside(false);
                       loadingBar.show();
                       com.example.e_ba9al.utilisateurs.Client c = new com.example.e_ba9al.utilisateurs.Client(_userNameInput,_clientPhoneInput,_passwordInput,_epecierPhoneInput);
                       inserClientToFirebase(c);
                       return true;

                   }
               }

                    return false;
                // data is okey

            }


            private void inserClientToFirebase(com.example.e_ba9al.utilisateurs.Client client){
                Firebase.clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(!(snapshot.child("BD_E_BA9AL_Clients").child(client.getUserName()).exists())){


                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL_Clients");
                            Query query = reference2.orderByChild("username");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String nomClient_at_i;
                                    String numeroTelEpicier_at_i;
                                    String numeroTelClient_at_i;
                                    String userNameCourant = client.getUserName();
                                    boolean findElementOnDBPanier = false;
                                    boolean numeroTelephoneDEpicier = false;//true si le numéro de téléphone correspontd à un épicier, et false sinon.
                                    boolean numeroTelephoneUnique = true;//true si le numéro de téléphone n'existe pas encore dans la base de données, et false sinon.
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        if (dataSnapshot.hasChild("username")) {
                                            nomClient_at_i = dataSnapshot.child("username").getValue(String.class);
                                            Log.d("idid", "client i : " + nomClient_at_i);
                                            if(nomClient_at_i.equals(userNameCourant))
                                                findElementOnDBPanier = true;
                                        }
                                    }
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        if (dataSnapshot.hasChild("phoneNumber") && dataSnapshot.hasChild("epicierPhoneNum")) {
                                            numeroTelEpicier_at_i = dataSnapshot.child("phoneNumber").getValue(String.class);
                                            String epicierPhoneNumber = dataSnapshot.child("epicierPhoneNum").getValue(String.class);
                                            if(numeroTelEpicier_at_i.equals(epecierPhoneInput.getText().toString()) && epicierPhoneNumber.equals("0"))
                                                numeroTelephoneDEpicier = true;
                                        }
                                    }
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        if (dataSnapshot.hasChild("phoneNumber")) {
                                            numeroTelClient_at_i = dataSnapshot.child("phoneNumber").getValue(String.class);
                                            Log.d("iddi", "numeroTelClient_at_i" + numeroTelClient_at_i);
                                            Log.d("iddi", "numeroTelClient_at_i" + numeroTelClient_at_i);
                                            Log.d("iddi", "numeroTelephoneUnique" + numeroTelephoneUnique);
                                            if(numeroTelClient_at_i.equals(clientPhoneInput.getText().toString()))
                                                numeroTelephoneUnique = false;
                                        }
                                    }
                                    if(findElementOnDBPanier == true) {
                                        Log.d("idid", "username déja utilisé");
                                        loadingBar.dismiss();
                                        clientUsernameInput.setError("اسم المستخدم مستعمل من قبل");
                                        clientUsernameInput.requestFocus();
                                    }
                                    else if(numeroTelephoneUnique == false){
                                        loadingBar.dismiss();
                                        clientPhoneInput.setError("رقم الهاتف مستخدم من قبل");
                                        clientPhoneInput.requestFocus();
                                    }
                                    else if(numeroTelephoneDEpicier == false){
                                        Log.d("idid", "username déja utilisé");
                                        loadingBar.dismiss();
                                        epecierPhoneInput.setError("رقم الهاتف لا يتعلق بأي بقال");
                                        epecierPhoneInput.requestFocus();
                                    }
                                    else{
                                        Log.d("idid", "C'est un username qui n'avait jamais éait utilisé");
                                        HashMap<String, Object> userdataMap = new HashMap<>();
                                        userdataMap.put("username",client.getUserName());
                                        userdataMap.put("password", client.getPassword());
                                        userdataMap.put("phoneNumber",client.getPhoneNumber());
                                        userdataMap.put("epicierPhoneNum", client.getEpicierPhoneNumber());

                                        Firebase.clientRef.child(client.getUserName()).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                                {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(CreerCompteClientActivity.this,"لقد تم إنشاء حسابكم بنجاح",Toast.LENGTH_LONG).show();
                                                    FirebaseUser user = mauth.getCurrentUser();
                                                    mauth.createUserWithEmailAndPassword(client.getUserName().concat(Help.appAaddress),client.getPassword())
                                                            .addOnCompleteListener(CreerCompteClientActivity.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                                                    Firebase.clientRef.child(mauth.getCurrentUser().getUid()).setValue(client);
                                                                    if(task.isSuccessful()){
                                                                        FirebaseUser user = mauth.getCurrentUser();
                                                                        loadingBar.dismiss();
                                                                        try{






                                                                            clientRef.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                                        if(dataSnapshot.child("username").getValue() != null && dataSnapshot.child("username").getValue().toString().equals(client.getUserName())){
                                                                                            if(!dataSnapshot.child("epicierPhoneNum").getValue().toString().equals("0")){
                                                                                                epicierPhoneNumber = dataSnapshot.child("epicierPhoneNum").getValue().toString();
                                                                                                for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                                                                                    if(dataSnapshot2.child("username").getValue() != null && dataSnapshot2.child("phoneNumber").getValue().toString().equals(epicierPhoneNumber) && dataSnapshot2.child("epicierPhoneNum").getValue().toString().equals("0")){

                                                                                                        if(inActivity == true) {
                                                                                                            userNameEpicier = dataSnapshot2.child("username").getValue().toString();
                                                                                                            Log.d("idid", "epicier : " + userNameEpicier);
                                                                                                            Log.d("iddi", "client : " + client.getUserName());


                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BD_E_BA9AL").child("Credit").child(userNameEpicier);
                                                                                                            Credit credit = new Credit(client.getUserName(), "0");
                                                                                                            reference.push().setValue(credit);

                                                                                                            loadingBar.dismiss();
                                                                                                            sessionManager.createSession(client.getUserName(), 0, userNameEpicier);
                                                                                                            intent = new Intent(CreerCompteClientActivity.this, AccueilClientActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            inActivity = false;
                                                                                                            break;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }catch(Exception e) {
                                                                            Log.w("TAG",e.getMessage(),e);
                                                                        }
                                                                    }else{

                                                                    }

                                                                }
                                                            });
                                                }
                                                else
                                                {
                                                    Toast.makeText(CreerCompteClientActivity.this,"المرجو إعادة المحاولة",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            Toast.makeText(CreerCompteClientActivity.this, "This " + client.getUserName() + " already exists.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Toast.makeText(CreerCompteClientActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

        });

    }

    public void redirectToLogin(View view){
        Intent intent = new Intent(CreerCompteClientActivity.this, AuthentificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("statue", "client");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

