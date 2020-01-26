package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_anstehende_termine extends AppCompatActivity {

    TextView mTermin1TextView;
    TextView mTermin2TextView;
    TextView mTermin3TextView;

    //Nachfolgend alles DB-Anbindung

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    //Termin1 ist die Bezeichnung des Datenbankobjekts



    DatabaseReference mConditionRef = mRootRef.child("termin1");
    DatabaseReference mConditionRef2 = mRootRef.child("termin2");
    DatabaseReference mConditionRef3 = mRootRef.child("termin3");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anstehende_termine);



        mTermin1TextView = (TextView)findViewById(R.id.textViewTermin1);
        mTermin2TextView = (TextView)findViewById(R.id.textViewTermin2);
        mTermin3TextView = (TextView)findViewById(R.id.textViewTermin3);

    }

    @Override
    protected void onStart() {
        super.onStart();

// Listener für Termin 1
        ValueEventListener valueEventListener = mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mTermin1TextView.setText(text);
            }






            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
// Listener für Termin 2
        ValueEventListener valueEventListener2 = mConditionRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mTermin2TextView.setText(text);
            }






            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
// Listener für Termin 3
                        ValueEventListener valueEventListener3 = mConditionRef3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String text = dataSnapshot.getValue(String.class);
                                mTermin3TextView.setText(text);
                            }






                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        }






                );





    }


}
