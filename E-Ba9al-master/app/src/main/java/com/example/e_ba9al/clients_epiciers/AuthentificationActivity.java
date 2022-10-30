package com.example.e_ba9al.clients_epiciers;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_ba9al.Assist.Help;
import com.example.e_ba9al.R;
import com.example.e_ba9al.clients.AccueilClientActivity;
import com.example.e_ba9al.clients.CreerCompteClientActivity;
import com.example.e_ba9al.epiciers.AccueilEpicierActivity;
import com.example.e_ba9al.epiciers.CreerComptEpicierActivity;
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
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AuthentificationActivity extends AppCompatActivity {
    EditText loginUsernameInput , loginPasswordInput;
    Button loginBtn,test;
    TextView registreBtn;
    FirebaseAuth mAuth;
    Intent intent;
    static String statue;
    static String userName;
    static ProgressDialog loadingBar;
    static String epicierPhoneNumber;
    static String userNameEpicier;
    static SessionManager sessionManager;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        userNameEpicier = "";
        loadingBar = new ProgressDialog(this);
        try {
            sessionManager = new SessionManager(this);

            Bundle bundle = getIntent().getExtras();
            statue = bundle.getString("statue");
            // make the keyboard hidden
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            loginUsernameInput = findViewById(R.id.loginUsernameInput);
            loginPasswordInput = findViewById(R.id.loginPasswordInput);
            loginBtn = findViewById(R.id.loginBtn);
            registreBtn = findViewById(R.id.registreBtn);
            mAuth = FirebaseAuth.getInstance();




            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingBar.setTitle("تسجيل الدخول");
                    loadingBar.show();
                    String emailUser = loginUsernameInput.getText().toString().trim().concat(Help.appAaddress);
                    String passwordUser = loginPasswordInput.getText().toString().trim();
                    if(TextUtils.isEmpty(emailUser)){
                        loadingBar.dismiss();
                        loginUsernameInput.setError("المرجو إدخال كلمة المرور");
                        loginUsernameInput.requestFocus();
                    }else if(TextUtils.isEmpty(passwordUser)){
                        loadingBar.dismiss();
                        loginPasswordInput.setError("المرجو إدخال كلمة المرور");
                        loginPasswordInput.requestFocus();
                    }else{
                        singInAccount(emailUser,passwordUser);
                    }
                }

                public void singInAccount(String _email,String _password){

                    mAuth.signInWithEmailAndPassword(_email, _password)
                            .addOnCompleteListener(AuthentificationActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        try{
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            userName = user.getEmail();
                                            userName = userName.substring(0, new StringBuffer(userName).indexOf("@"));
                                            Log.d("iddi", "dans la page de l'authentification : " + userName);
                                            clientRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                                                        if(dataSnapshot.child("username").getValue() != null && dataSnapshot.child("username").getValue().toString().equals(userName)){
                                                            Log.d("iddi", "username : " + dataSnapshot.child("username").getValue());
                                                            loadingBar.dismiss();
                                                            if(statue.equals("client") && !dataSnapshot.child("epicierPhoneNum").getValue().toString().equals("0")){
                                                                epicierPhoneNumber = dataSnapshot.child("epicierPhoneNum").getValue().toString();
                                                                for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                                                    if(dataSnapshot2.child("username").getValue() != null && dataSnapshot2.child("phoneNumber").getValue().toString().equals(epicierPhoneNumber)){
                                                                        userNameEpicier = dataSnapshot2.child("username").getValue().toString();
                                                                        Log.d("idid", "epicier : " + userNameEpicier);
                                                                        sessionManager.createSession(userName, 0, userNameEpicier);
                                                                        intent = new Intent(AuthentificationActivity.this, AccueilClientActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                }
                                                            }
                                                            else if(statue.equals("epicier") && dataSnapshot.child("epicierPhoneNum").getValue().toString().equals("0")){
                                                                sessionManager.createSession(userName, 1, "");
                                                                intent = new Intent(AuthentificationActivity.this, AccueilEpicierActivity.class);
                                                                startActivity(intent);
                                                            }
                                                            else{
                                                                loginUsernameInput.setError("المرجو إدخال اسم المستخدم بشكل صحيح");
                                                                loginUsernameInput.requestFocus();
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }catch (Exception e){
                                            Log.w("iddi",e.getMessage(),e);
                                        }

                                    } else {
                                        loadingBar.dismiss();
                                        mAuth.setLanguageCode("ar");
                                        if(task.getException().getMessage().charAt(3) == 'r'){
                                            loginUsernameInput.setError("اسم المستخدم غير صحيح");
                                            loginUsernameInput.requestFocus();
                                        }
                                        else if(task.getException().getMessage().charAt(3) == ' '){
                                            loginPasswordInput.setError("كلمة المرور غير صحيحة");
                                            loginPasswordInput.requestFocus();
                                        }

                                    }
                                }


                            });
                }

            });






            registreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(statue.equals("client"))
                        intent = new Intent(AuthentificationActivity.this, CreerCompteClientActivity.class);
                    else
                        intent = new Intent(AuthentificationActivity.this, CreerComptEpicierActivity.class);
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){
            Log.d("iddi", e.getMessage());
        }




    }
    public void authentificationClient(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("BD_E_BA9AL_Clients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if(dataSnapshot.child("epicierPhoneNum").getValue() != null && dataSnapshot.child("phoneNumber").getValue() != null && !dataSnapshot.child("epicierPhoneNum").getValue().toString().equals("0") && dataSnapshot.child("phoneNumber").getValue().toString().equals(epicierPhoneNumber)){
                            userNameEpicier = dataSnapshot.child("username").getValue().toString();
                            sessionManager.createSession(userName, 0, userNameEpicier);
                            intent = new Intent(AuthentificationActivity.this, AccueilClientActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                catch (Exception e){
                    Log.d("iddi", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {//Quand on clique sur le Back Button, on ne va pas appliquer ce qui s'applique d'abitude, mais on va le forcer à faire d'autres choses.
        Intent intent = new Intent(AuthentificationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

