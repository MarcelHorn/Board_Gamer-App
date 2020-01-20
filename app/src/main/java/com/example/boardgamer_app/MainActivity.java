package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


TextView mConditionTextView;
Button mButtonSunny;
Button mButtonFoggy;

DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
DatabaseReference mConditionRef = mRootRef.child("condition");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConditionTextView = (TextView)findViewById(R.id.textViewCondition);
        mButtonSunny = (Button)findViewById(R.id.buttonSunny);
        mButtonFoggy = (Button)findViewById(R.id.buttonFoggy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener valueEventListener = mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mConditionTextView.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        mButtonSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConditionRef.setValue("Sunny");

            }
        });

        mButtonFoggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConditionRef.setValue("Foggy");

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

    public void onClickNeuerTermin (View Button) {
        Intent changeIntent = new Intent (MainActivity.this, Main7Activity.class);
        startActivity(changeIntent);
    }
}
