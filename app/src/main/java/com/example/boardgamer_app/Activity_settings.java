package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.CheckInternet;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_settings extends AppCompatActivity {

    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Button Logout
        btnLogout = findViewById(R.id.btn_logout);

        //Listener, der den User bei Klick auf den Abmelden-Button abmeldet mit der Methode ".signOut()" und anschließend zur Login Seite wechselt
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();

                Intent intToMain = new Intent(Activity_settings.this, Activity_login.class);
                startActivity(intToMain);
            }
        });

    }

    public void onClickGruppen (View Button)
    {
        if (CheckInternet.isNetwork(Activity_settings.this)) {
            Intent changeIntent = new Intent (Activity_settings.this, Activity_settings_evenings.class);
            startActivity(changeIntent);
        }
        else Toast.makeText(Activity_settings.this, CheckInternet.NO_CONNECTION, Toast.LENGTH_SHORT).show();

    }

    public void onClickProfil (View Button)
    {
        Intent changeIntent = new Intent (Activity_settings.this, Activity_settings_user.class);
        startActivity(changeIntent);
    }
}
