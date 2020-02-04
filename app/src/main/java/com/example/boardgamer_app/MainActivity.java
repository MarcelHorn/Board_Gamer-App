package com.example.boardgamer_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    //Keys
    public static final String KEY_NAME = "name";

    //optional: ist der TAG der Exception, um mögliche Fehler zu lokalisieren

    private static final String TAG = "mainActivity";

    //Gesamte Datenbank Instanz
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



    }


    public void onClickTest (View v) {
        DatabaseController data = new DatabaseController();

        data.db.collection(DatabaseController.USER_COL)
                .document(data.mFirebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String id = documentSnapshot.getString("id");
                            Toast.makeText(MainActivity.this, id, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void onClickNachrichten (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Main2Activity.class);
        startActivity(changeIntent);
           }

    public void onClickEinstellungen (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Main3Activity.class);
        startActivity(changeIntent);
    }

    public void onClickGruppenErstellung (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Activity_create_group.class);
        startActivity(changeIntent);
    }

    public void onClickAnstehendeTermine (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Activity_anstehende_termine.class);
        startActivity(changeIntent);
    }

}
