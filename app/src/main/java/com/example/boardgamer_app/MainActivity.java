package com.example.boardgamer_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {


    //Keys
    public static final String KEY_NAME = "name";

    private int userId;
    private String userName;

    //optional: ist der TAG der Exception, um mögliche Fehler zu lokalisieren

    private static final String TAG = "mainActivity";

    //Gesamte Datenbank Instanz
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseController databaseController = new DatabaseController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //Laden der User Id aus der Datenbank
        databaseController.db
                .collection(DatabaseController.USER_COL)
                .document(databaseController.mFirebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        userId = documentSnapshot.getLong("id").intValue();
                        userName = documentSnapshot.getString("name");
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseController.CheckEvenings();
    }

    public void onClickTest (View Button) {
        databaseController.GenerateNewEvening();
    }

    public void onClickNachrichten (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Activity_messages.class);
        changeIntent.putExtra("UserName", userName);
        startActivity(changeIntent);
           }

    public void onClickEinstellungen (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Activity_settings.class);
        startActivity(changeIntent);
    }

    public void onClickVergangeneTermine (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Activity_past_evenings.class);
        changeIntent.putExtra("UserNameId", userId);
        startActivity(changeIntent);
    }

    public void onClickAnstehendeTermine (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Activity_evenings.class);
        startActivity(changeIntent);
    }

}
