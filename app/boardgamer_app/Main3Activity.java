package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void onClickGruppen (View Button) {
        Intent changeIntent = new Intent (Main3Activity.this, Main4Activity.class);
        startActivity(changeIntent);
    }

    public void onClickBenachrichtigungen (View Button) {
        Intent changeIntent = new Intent (Main3Activity.this, Main5Activity.class);
        startActivity(changeIntent);
    }

    public void onClickProfil (View Button) {
        Intent changeIntent = new Intent (Main3Activity.this, Main6Activity.class);
        startActivity(changeIntent);
    }
}
