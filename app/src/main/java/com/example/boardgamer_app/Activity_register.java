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

        //Firebase Instanz für Authentification
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Eingabefelder E-Mail und Passwort
        emailId = findViewById(R.id.editText_register_mail);
        password = findViewById(R.id.editText_register_password);

        //Button Register
        btnSignUp = findViewById(R.id.btn_register);

        //simpler onClickListener, der die Eingaben von E-Mail und Passwort prüft, beim Klick auf den Button
        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pw = password.getText().toString();


                if (email.isEmpty() && pw.isEmpty())
                {
                    Toast.makeText(Activity_register.this, "Felder sind leer!", Toast.LENGTH_SHORT).show();
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
                //sind alle Felder ausgefüllt folgt der VERSUCH in die Datenbank zu schreiben. Der OnCompleteListener prüft, ob die Methode "createUserWithEmailAndPassword" erfolgreich war
                //Ein Fehler wäre z.B. eine ungültige E-Mail Formatierung (z.B. ohne "@") oder ein Passwort mit weniger als 6 Zeichen
                else if (!(email.isEmpty() && pw.isEmpty()))
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(Activity_register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(Activity_register.this,"Registrierung fehlerhaft, bitte erneut versuchen",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Activity_register.this,"Erfolgreich registriert!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Activity_register.this,MainActivity.class));
                            }
                        }
                    });
                }
                else
                {
                    //Hier dürfte der Compiler nie hinkommen, da beide Textfelder leer und voll sein müssten, aber sicher ist sicher. (vielleicht mögliche Ladefehler)
                    Toast.makeText(Activity_register.this,"unbekannter Fehler!",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
