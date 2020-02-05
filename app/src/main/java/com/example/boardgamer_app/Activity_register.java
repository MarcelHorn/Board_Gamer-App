package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_register extends AppCompatActivity {

    private static final String TAG = "Activity_register" ;
    EditText name,emailId, password;
    Button btnSignUp;
    DatabaseController databaseController = new DatabaseController();
    //FirebaseAuth mFirebaseAuth;
    //Instanz zur Datenbank
    //FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase Instanz für Authentification
        //mFirebaseAuth = FirebaseAuth.getInstance();

        //Eingabefelder Name, E-Mail und Passwort
        name = findViewById(R.id.editText_register_name);
        emailId = findViewById(R.id.editText_register_mail);
        password = findViewById(R.id.editText_register_password);

        //Button Register
        btnSignUp = findViewById(R.id.btn_register);

        //simpler onClickListener, der die Eingaben von E-Mail und Passwort prüft, beim Klick auf den Button
        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString();
                String pw = password.getText().toString();
                final String username = name.getText().toString();


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
                else if (!(email.isEmpty() && pw.isEmpty() && username.isEmpty()))
                {

                    databaseController.mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(Activity_register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(Activity_register.this,"Registrierung fehlerhaft, bitte erneut versuchen",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                databaseController.db.collection(DatabaseController.GROUP_COL)
                                        .document(DatabaseController.GROUP_SETTINGS_DOC)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    int idIndex = documentSnapshot.getLong("AnzahlMitgliederIndex").intValue();
                                                    idIndex = idIndex + 1;

                                                    //Hashmaps war Java I oder II? Aufjedenfall Collection aus Schlüssel-Wert Paaren, um gleich die Felder zu bestimmen
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put(MainActivity.KEY_NAME, username);
                                                    data.put("inGroup", true);
                                                    data.put("isAdmin", false);
                                                    data.put("id", idIndex);


                                                    Toast.makeText(Activity_register.this, "Erfolgreich registriert!", Toast.LENGTH_SHORT).show();

                                                    //FireStore kategorisiert in folgener Reihenfolge: Collection(Sammlung) > Document(document) > Feld
                                                    //Collection = "User", document = "Email-Adresse", Felder = alle der Hash-Map oben
                                                    databaseController.writeInDatabase(DatabaseController.USER_COL, email.toLowerCase(), data);
                                                    databaseController.db.collection(DatabaseController.GROUP_COL)
                                                            .document(DatabaseController.GROUP_SETTINGS_DOC)
                                                            .update("AnzahlMitgliederIndex", idIndex);


                                                    startActivity(new Intent(Activity_register.this, MainActivity.class));
                                                }
                                            }
                                        });

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
