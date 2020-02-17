package com.example.boardgamer_app.Classes;


import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class DatabaseController {

    public final static String DEBUGTAG = "BGAppDebug";
    private static final String TAG = "DatabaseController";

    public final static String USER_COL = "User";
    public final static String EVENING_COL = "Termine";
    public final static String GROUP_COL = "Gruppe";
    public final static String PAST_EVENING_COL = "Vergangene Termine";
    public final static String MESSAGE_COL = "Nachrichten";
    public final static String GAMES_COL = "Spielevorschläge";

    public final static String GROUP_SETTINGS_DOC = "Gruppeneinstellungen";

    public FirebaseAuth mFirebaseAuth;
    public FirebaseFirestore db;

    Timestamp ts;
    int eveningId, intervalIndex, interval, eveningNameId, organizerId, memberCount;

    CollectionReference usersCol, groupsCol, eveningsCol;
    DocumentReference groupSettingsDoc;

    public DatabaseController() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        usersCol = db.collection(DatabaseController.USER_COL);
        groupsCol = db.collection(DatabaseController.GROUP_COL);
        eveningsCol = db.collection(DatabaseController.EVENING_COL);

        groupSettingsDoc = db.collection(DatabaseController.GROUP_COL)
                             .document(DatabaseController.GROUP_SETTINGS_DOC);

        eveningId = 0;
    }

    //überschreibt alle Felder des Dokuments mit der neuen Map (nicht erwähnte werden gelöscht)
    public void writeInDatabase (String collection, String document, Map<String, Object> field) {
        db.collection(collection)
                .document(document)
                .set(field, SetOptions.merge())
                //Wenn erfolgreich
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void xvoid) {
                        Log.d(DEBUGTAG, "Success!");
                    }
                })
                //Bei Lade-Fehler Exception
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DEBUGTAG, "Failure!");
                    }
                });
    }

    //Überschreibt nur die in der Map erwähnten Felder ohne nicht erwähnte zu löschen
    public void UpdateDatabase (String collection, String document, Map<String, Object> field) {
        db.collection(collection)
                .document(document)
                .update(field)
                //Wenn erfolgreich
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void xvoid) {
                        Log.d(DEBUGTAG, "Success!");
                    }
                })
                //Bei Lade-Fehler Exception
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DEBUGTAG, "Failure!");
                    }
                });
    }

    public void UpdateDatabaseOneField (String collection, String document, String field, Object value) {
        db.collection(collection)
                .document(document)
                .update(field, value)
                //Wenn erfolgreich
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void xvoid) {
                        Log.d(DEBUGTAG, "Success!");
                    }
                })
                //Bei Lade-Fehler Exception
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DEBUGTAG, "Failure!");
                    }
                });
    }


    public void GenerateNewEvening() {
        //TODO: Wenn ein Termin abgehalten wurde, muss 1 neuer Termin erstellt werden
        //1. Suche ältester Termin: speichere timestamp und id
        //2. Addiere intervall drauf
        //3. Erstelle neuen Termin

        eveningsCol.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ts = new Timestamp(0,0);

                                for (DocumentSnapshot snapshots : task.getResult()) {
                                    if (snapshots.getTimestamp("Datum").getSeconds() > ts.getSeconds()) {
                                        ts = snapshots.getTimestamp("Datum");
                                        eveningId = snapshots.getLong("id").intValue();
                                        eveningNameId = Integer.parseInt(snapshots.getReference().getId().substring(6));
                                        organizerId = snapshots.getLong("Organizer").intValue();
                                    }

                                }

                                Log.d(TAG, "timestamp= " + ts.toDate().toString());
                                Log.d(TAG, "eveningId= " + eveningId);
                                Log.d(TAG, "eveningNameId= " + eveningNameId);

                                groupSettingsDoc.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        intervalIndex = task.getResult().getLong("RhythmusIndex").intValue();
                                                        memberCount = task.getResult().getLong("AnzahlMitgliederIndex").intValue();
                                                        Log.d(TAG, "intervallIndex= " + intervalIndex);

                                                        //Methode, um aus dem intervallIndex den tatsächlichen Intervall zu erhalten
                                                        GetInterval();

                                                        organizerId++;
                                                        eveningNameId++;
                                                        eveningId++;

                                                        if (organizerId > memberCount) {
                                                            organizerId = 1;
                                                        }
                                                        //


                                                        Calendar cal = Calendar.getInstance();
                                                        cal.setTime(ts.toDate());
                                                        cal.add(Calendar.DATE, interval);
                                                        Timestamp newTs = new Timestamp(cal.getTime());
                                                        Log.d(TAG, "onComplete: "+ newTs.toDate().toString());

                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("Datum", newTs);
                                                        data.put("id", eveningId);
                                                        data.put("Organizer", organizerId);


                                                        writeInDatabase(DatabaseController.EVENING_COL, "Termin"+ eveningNameId, data);
                                                    }
                                            }
                                        });
                            }
                    }
                });


    }

    private void GetInterval() {
        switch (intervalIndex)
        {
            case 0:
                interval = 7;
                intervalIndex = 0;
                break;
            case 1:
                interval = 14;
                intervalIndex = 1;
                break;
            case 2:
                interval = 21;
                intervalIndex = 2;
                break;
            default:
                interval = 0;
                intervalIndex = 0;
                break;
        }
    }

    public void SetEveningAsCompleted() {
        //TODO: Verschiebe abgehaltenen Termin von Anstehende Termine zu vergangende Termine
    }
}
