package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Activity_anstehende_termine extends AppCompatActivity {

    //Interface Callback Funktion
    private interface FirestoreCallback {
        void onCallback(List<Timestamp> eveningTimestampList, List<Integer> eveningOrganizerList);
    }

    CollectionReference eveningCollection;
    TextView mTermin1TextView;
    TextView mTermin2TextView;
    TextView mTermin3TextView;
    ArrayList<Timestamp> timestampList = new ArrayList();
    ArrayList<Integer> eveningOrganizerList = new ArrayList();
    Button[] buttons = new Button[5];

    //Nachfolgend alles DB-Anbindung

    DatabaseController databaseController = new DatabaseController();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    //Termin1 ist die Bezeichnung des Datenbankobjekts



    DatabaseReference mConditionRef = mRootRef.child("termin1");
    DatabaseReference mConditionRef2 = mRootRef.child("termin2");
    DatabaseReference mConditionRef3 = mRootRef.child("termin3");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anstehende_termine);

        eveningCollection = databaseController.db.collection(DatabaseController.EVENING_COL);

        buttons[0] = (Button) findViewById(R.id.btnEvening1);
        buttons[1] = (Button) findViewById(R.id.btnEvening2);
        buttons[2] = (Button) findViewById(R.id.btnEvening3);
        buttons[3] = (Button) findViewById(R.id.btnEvening4);
        buttons[4] = (Button) findViewById(R.id.btnEvening5);




        mTermin1TextView = (TextView)findViewById(R.id.textViewTermin1);
        mTermin2TextView = (TextView)findViewById(R.id.textViewTermin2);
        mTermin3TextView = (TextView)findViewById(R.id.textViewTermin3);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ReadEveningTimestamps(new FirestoreCallback() {
            @Override
            public void onCallback(List<Timestamp> eveningTimestampList, List<Integer> eveningOrganizerList) {
                SimpleDateFormat sdfTime = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
                for(int i = 0; i < timestampList.size(); i++) {

                    Log.d(DatabaseController.DEBUGTAG,timestampList.get(i).toString());
                    Log.d(DatabaseController.DEBUGTAG,eveningOrganizerList.get(i).toString());
                    String time = sdfTime.format(timestampList.get(i).toDate());
                    buttons[i].setText(time + " Uhr, Bei: " + eveningOrganizerList.get(i).toString());
                }
            }
        });



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

                        });
    }

    //Methode zur Callback Funktion
    private void ReadEveningTimestamps(final FirestoreCallback firestoreCallback) {
        eveningCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshots : task.getResult()) {
                                Timestamp ts = snapshots.getTimestamp("Datum");
                                timestampList.add(ts);
                                int i = snapshots.getLong("Organizer").intValue();
                                eveningOrganizerList.add(i);
                            }
                            firestoreCallback.onCallback(timestampList, eveningOrganizerList );
                        }
                    }
                });
    }
}
