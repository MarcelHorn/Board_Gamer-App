package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Main3Activity extends AppCompatActivity {

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

                Intent intToMain = new Intent(Main3Activity.this, Activity_login.class);
                startActivity(intToMain);
            }
        });

    }

    public void onClickGruppen (View Button)
    {
        Intent changeIntent = new Intent (Main3Activity.this, Main4Activity.class);
        startActivity(changeIntent);
    }

    public void onClickBenachrichtigungen (View Button)
    {
        Intent changeIntent = new Intent (Main3Activity.this, Main5Activity.class);
        startActivity(changeIntent);
    }

    public void onClickProfil (View Button)
    {
        Intent changeIntent = new Intent (Main3Activity.this, Main6Activity.class);
        startActivity(changeIntent);
    }
}
