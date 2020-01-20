package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

kjhkjhkjh

public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference myRef = database.getReference();
    myRef.addValueEventLstener(new ValueEventlistener) {
        @override
                public void onDataChange(Datasnapshot dataSnaphot){
            String x = dataSnapshot.getValue(String.class);
            textView2.setText(x);

        }
        @override
                public void onCancelled(DatabaseError databaseError) {

        }
    }

    TextView textView2 = (TextView)findViewByID(R.id.textView);

    public MainActivity() {
        database = FirebaseDatabase.getInstance();
    }


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

    public void onClickNeuerTermin (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Main7Activity.class);
        startActivity(changeIntent);
    }
}
