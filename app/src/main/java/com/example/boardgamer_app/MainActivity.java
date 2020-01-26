package com.example.boardgamer_app;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
