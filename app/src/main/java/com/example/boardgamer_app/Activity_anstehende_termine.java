package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

    private ListView listView;
    private ArrayList<String> listViewObjects;
    private ArrayAdapter<String> eveningAdapter;
    CollectionReference eveningCollection;
    TextView mTermin1TextView;
    TextView mTermin2TextView;
    TextView mTermin3TextView;
    ArrayList<Timestamp> timestampList = new ArrayList();
    ArrayList<Integer> eveningOrganizerList = new ArrayList();
    Button[] buttons = new Button[5];

    //Nachfolgend alles DB-Anbindung

    DatabaseController databaseController = new DatabaseController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anstehende_termine);

        listViewObjects = new ArrayList<>();
        eveningCollection = databaseController.db.collection(DatabaseController.EVENING_COL);
        listView = findViewById(R.id.listview);


    }

    @Override
    protected void onStart() {
        super.onStart();



        ReadEveningTimestamps(new FirestoreCallback() {
            @Override
            public void onCallback(List<Timestamp> eveningTimestampList, List<Integer> eveningOrganizerList) {


                SimpleDateFormat sdfTime = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm");
                for(int i = 0; i < timestampList.size(); i++) {

                    String time = sdfTime.format(timestampList.get(i).toDate());
                    listViewObjects.add(time + " Bei: " + eveningOrganizerList.get(i).toString());
                    //buttons[i].setText(time + " Uhr, Bei: " + );
                }
                listView.setAdapter(new ArrayAdapter<String>(
                        Activity_anstehende_termine.this,
                        android.R.layout.simple_list_item_1,
                        listViewObjects
                ));
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
