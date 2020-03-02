package com.example.boardgamer_app.Classes;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

    public final static int SECONDS_AFTER_NOW_FOR_PAST_EVENING = 14400; //4 Stunden

    public FirebaseAuth mFirebaseAuth;
    public FirebaseFirestore mDatabase;

    Timestamp ts;
    int eveningId, intervalIndex, interval, eveningNameId, organizerId, memberCount;

    CollectionReference usersCol, groupsCol, eveningsCol, pastEveningsCol;
    DocumentReference groupSettingsDoc;

    List<DocumentSnapshot> documentSnapshotList;

    public DatabaseController() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        usersCol = mDatabase.collection(DatabaseController.USER_COL);
        groupsCol = mDatabase.collection(DatabaseController.GROUP_COL);
        eveningsCol = mDatabase.collection(DatabaseController.EVENING_COL);
        pastEveningsCol = mDatabase.collection(DatabaseController.PAST_EVENING_COL);

        groupSettingsDoc = mDatabase.collection(DatabaseController.GROUP_COL)
                             .document(DatabaseController.GROUP_SETTINGS_DOC);

        eveningId = 0;

        documentSnapshotList = new ArrayList<>();
    }

    //überschreibt alle Felder des Dokuments mit der neuen Map (nicht erwähnte werden gelöscht)
    public void writeInDatabase (String collection, String document, Map<String, Object> field) {
        mDatabase.collection(collection)
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

    //überschreibt alle Felder des Dokuments mit der neuen Map (nicht erwähnte werden gelöscht)
    public void writeInDatabase (final Context context, String collection, String document, Map<String, Object> field) {
        mDatabase.collection(collection)
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
                        Toast.makeText(context, "Daten konnten nicht gespeichert werden", Toast.LENGTH_SHORT).show();
                        Log.d(DEBUGTAG, "Failure!");
                    }
                });
    }

    //Überschreibt nur die in der Map erwähnten Felder ohne nicht erwähnte zu löschen
    public void UpdateDatabase (String collection, String document, Map<String, Object> field) {
        mDatabase.collection(collection)
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
        mDatabase.collection(collection)
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
        //1. Suche ältester Termin: speichere timestamp und id
        //2. Addiere intervall drauf
        //3. Erstelle neuen Termin

        eveningsCol.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                            if (task.isSuccessful())
                            {
                                ts = new Timestamp(0,0);
                                for (DocumentSnapshot snapshots : task.getResult())
                                {
                                    if (snapshots.getTimestamp("Datum").getSeconds() > ts.getSeconds())
                                    {
                                        ts = snapshots.getTimestamp("Datum");
                                        eveningId = snapshots.getLong("id").intValue();
                                        organizerId = snapshots.getLong("Organizer").intValue();

                                        //Die Id des TerminDokuments, also bei z.B. Termin5 = 5
                                        eveningNameId = Integer.parseInt(snapshots.getReference().getId().substring(6));

                                    }
                                }

                                Log.d(TAG, "timestamp= " + ts.toDate().toString());
                                Log.d(TAG, "eveningId= " + eveningId);
                                Log.d(TAG, "eveningNameId= " + eveningNameId);

                                groupSettingsDoc.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                            {
                                                    if (task.isSuccessful())
                                                    {
                                                        intervalIndex = task.getResult().getLong("RhythmusIndex").intValue();
                                                        memberCount = task.getResult().getLong("AnzahlMitgliederIndex").intValue();
                                                        Log.d(TAG, "intervallIndex= " + intervalIndex);

                                                        //Methode, um aus dem intervallIndex den tatsächlichen Intervall zu erhalten
                                                        GetInterval();

                                                        organizerId++;
                                                        eveningNameId++;
                                                        eveningId++;

                                                        if (organizerId > memberCount)
                                                        {
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

    private void GetInterval()
    {
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

    public void CheckEvenings()
    {
        //1. Lade alle aktuellen Termine (für Timestamp, Organizer und id)
        //2. Prüfe die timestamps mit dem aktuellen Zeitpunkt
        //3. Liegt mindestens 1 Termin in Vergangenheit lade alle vergangenen Termine (für id)
        //4. Abspeichern in vergangene Termine mit neuer id
        //5. Löschen in aktuelle Termine

        eveningsCol.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            documentSnapshotList.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                if (snapshot.getTimestamp("Datum").getSeconds() < Timestamp.now().getSeconds() + SECONDS_AFTER_NOW_FOR_PAST_EVENING) {
                                    documentSnapshotList.add(snapshot);
                                }
                            }
                            Log.d(TAG, "snapshotlist= " + documentSnapshotList.toString());
                            pastEveningsCol.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                int newId = 0;
                                                if (task.isSuccessful() && task.getResult() != null) {
                                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                                        if (snapshot.getLong("id").intValue() > newId ) {
                                                            newId = snapshot.getLong("id").intValue();
                                                        }
                                                    }
                                                }

                                                newId++;
                                                for (DocumentSnapshot snapshot2 : documentSnapshotList) {
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("Datum", snapshot2.getTimestamp("Datum"));
                                                    data.put("Organizer", snapshot2.getLong("Organizer").intValue());
                                                    data.put("id", newId);
                                                    data.put("BewertungVer", 0);
                                                    data.put("BewertungFood", 0);
                                                    data.put("BewertungAllg", 0);
                                                    data.put("AnzahlBewertungen", 0);

                                                    Log.d(TAG, "Map= " +data.toString());
                                                    writeInDatabase(DatabaseController.PAST_EVENING_COL, "Termin" + newId, data);
                                                    snapshot2.getReference().delete();
                                                    GenerateNewEvening();
                                                }
                                        }
                                    });

                        }
                    }
                });
    }

}
