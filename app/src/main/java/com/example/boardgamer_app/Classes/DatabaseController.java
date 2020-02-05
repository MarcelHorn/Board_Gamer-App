package com.example.boardgamer_app.Classes;


import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.widget.ActionMenuView;
import android.widget.Toast;

import com.example.boardgamer_app.Activity_register;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Map;

import androidx.annotation.NonNull;

public class DatabaseController {

    public final static String DEBUGTAG = "BGAppDebug";

    public final static String USER_COL = "User";
    public final static String EVENING_COL = "Termine";
    public final static String GROUP_COL = "Gruppe";
    public final static String GROUP_SETTINGS_DOC = "Gruppeneinstellungen";

    public FirebaseAuth mFirebaseAuth;
    public FirebaseFirestore db;
    private String keyPlaceholder;

    public String getResult() {
        return result;
    }

    private String result;

    public DatabaseController() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void writeInDatabase (String collection, String document, Map<String, Object> field) {
        db.collection(collection)
                .document(document)
                .set(field)
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
        public void writeInDatabaseAsTimestamp(String collection, String document, Map<String, Timestamp> field) {
            db.collection(collection)
                    .document(document)
                    .set(field)
                    //Wenn erfolgreich
                    .addOnSuccessListener(new OnSuccessListener<Void>(){
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

        public void loadFromDatabase (String collection, String document, String key) {
                keyPlaceholder = key;
                db.collection(collection)
                        .document(document)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    result = documentSnapshot.getString(keyPlaceholder);
                                    Log.d(DEBUGTAG, "Loading Success!");
                                } else {
                                    Log.d(DEBUGTAG, "Loading Failure!");
                                }
                            }
                        });


        }


}
