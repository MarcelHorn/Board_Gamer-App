package com.example.boardgamer_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "mainActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



    }


    public void onClickTest (View v) {

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
