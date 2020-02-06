package com.example.boardgamer_app.Classes;


import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

    //überschreibt alle Felder des Dokuments mit der neuen Map (nicht erwähnte werden gelöscht)
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
}
