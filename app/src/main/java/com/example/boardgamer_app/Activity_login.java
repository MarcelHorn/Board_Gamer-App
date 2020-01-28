package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_login extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase Instanz für Authentification
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Eingabefelder E-Mail und Passwort
        emailId = findViewById(R.id.editText_login_mail);
        password = findViewById(R.id.editText_login_password);

        //Button login
        btnSignIn = findViewById(R.id.btn_login);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(Activity_login.this,"Du bist angemeldet!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Activity_login.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(Activity_login.this,"Bitte anmelden",Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pw = password.getText().toString();

                if (email.isEmpty() && pw.isEmpty())
                {
                    Toast.makeText(Activity_login.this, "Felder sind leer!", Toast.LENGTH_SHORT).show();
                }
                else if (pw.isEmpty())
                {
                    password.setError("Bitte Passwort eingeben!"); //rotes Ausrufezeichen am Passwort-Feld
                    password.requestFocus(); //Setzt den Cursor auf das Passwort-Feld
                }
                else if (email.isEmpty())
                {
                    emailId.setError("Bitte E-Mail eingeben!"); //gleiche für e-mail
                    emailId.requestFocus();
                }
                else  if(!(email.isEmpty() && pw.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(Activity_login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Activity_login.this,"Anmelden gescheitert, bitte erneut versuchen",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToHome = new Intent(Activity_login.this,MainActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Activity_login.this,"unbekannter Fehler!",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onClickRegister (View Button) {
        Intent changeIntent = new Intent (this, Activity_register.class);
        startActivity(changeIntent);
    }
}
