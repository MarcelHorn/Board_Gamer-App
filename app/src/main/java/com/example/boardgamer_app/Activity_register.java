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

public class Activity_register extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText_register_mail);
        password = findViewById(R.id.editText_register_password);

        btnSignUp = findViewById(R.id.btn_register);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pw = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Bitte E-Mail eingeben!");
                    emailId.requestFocus();
                }
                else if (pw.isEmpty()) {
                    password.setError("Bitte Passwort eingeben!");
                    password.requestFocus();
                }
                else if (email.isEmpty() && pw.isEmpty()) {
                    Toast.makeText(Activity_register.this, "Felder sind leer!", Toast.LENGTH_SHORT);
                }
                else if (!(email.isEmpty() && pw.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(Activity_register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Activity_register.this,"Registrierung fehlerhaft, bitte erneut versuchen",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Activity_register.this,"Erfolgreich registriert!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Activity_register.this,MainActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Activity_register.this,"unbekannter Fehler!",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
