package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.CheckInternet;
import com.example.boardgamer_app.Classes.DatabaseController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_evenings extends AppCompatActivity {

    //Interface Callback Funktion
    private interface FirestoreCallback {
        void onCallback(List<Timestamp> eveningTimestampList, List<Integer> eveningOrganizerList, List<Integer> eveningIdList);
    }

    private static final String TAG = "Activity_anstehende_ter";

    private ListView listView;
    private ArrayList<String> listViewObjects;
    private CollectionReference eveningCollection;
    private Map<Integer, String> userData;

    ArrayList<Timestamp> timestampList = new ArrayList();
    ArrayList<Integer> eveningOrganizerList = new ArrayList();
    ArrayList<Integer> eveningIdList = new ArrayList<>();

    DatabaseController databaseController = new DatabaseController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anstehende_termine);

        listViewObjects = new ArrayList<>();
        userData = new HashMap<>();
        eveningCollection = databaseController.mDatabase.collection(DatabaseController.EVENING_COL);
        listView = findViewById(R.id.listview);

        databaseController.mDatabase.collection(DatabaseController.USER_COL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshots : task.getResult()) {

                                String name = snapshots.getString("name");
                                int id = snapshots.getLong("id").intValue();
                                userData.put(id, name);
                            }
                            Log.d(TAG, userData.toString());
                        ReadEveningTimestamps(new FirestoreCallback() {
                            @Override
                            public void onCallback(List<Timestamp> eveningTimestampList, List<Integer> eveningOrganizerList, List<Integer> eveningIdList) {

                                if (listViewObjects.isEmpty()) {


                                    SimpleDateFormat sdfTime = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm");
                                    for (int i = 0; i < timestampList.size(); i++) {

                                        String time = sdfTime.format(timestampList.get(i).toDate());

                                        if (userData.containsKey(eveningOrganizerList.get(i))) {
                                            String name = userData.get(eveningOrganizerList.get(i));
                                            listViewObjects.add(time + " Bei: " + name);
                                        }

                                        //buttons[i].setText(time + " Uhr, Bei: " + );
                                    }
                                    listView.setAdapter(new ArrayAdapter<String>(
                                            Activity_evenings.this,
                                            android.R.layout.simple_list_item_1,
                                            listViewObjects
                                    ));
                                }
                            }
                        });

                    }
                }
                });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (CheckInternet.isNetwork(Activity_evenings.this)) {
                    Intent intent = new Intent(Activity_evenings.this, Activity_evening_details.class);

                    long longTime = timestampList.get(position).getSeconds()*1000;
                    intent.putExtra("Timestamp", longTime);
                    intent.putExtra("Organizer", userData.get(eveningOrganizerList.get(position)));
                    intent.putExtra("id",  eveningIdList.get(position));
                    startActivity(intent);
                }
                else Toast.makeText(Activity_evenings.this, CheckInternet.NO_CONNECTION, Toast.LENGTH_SHORT).show();


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
                                int id = snapshots.getLong("id").intValue();
                                eveningIdList.add(id);

                            }
                            firestoreCallback.onCallback(timestampList, eveningOrganizerList, eveningIdList );
                        }
                    }
                });
    }
}
