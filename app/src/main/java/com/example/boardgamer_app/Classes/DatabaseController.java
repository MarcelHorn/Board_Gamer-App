package com.example.boardgamer_app.Classes;


import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.widget.ActionMenuView;
import android.widget.Toast;

import com.example.boardgamer_app.Activity_register;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Map;

import androidx.annotation.NonNull;

public class DatabaseController {

    public final static String DEBUGTAG = "BGAppDebug";

    public FirebaseAuth mFirebaseAuth;
    public FirebaseFirestore db;

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
        public void writeInDatabaseAsCalendar (String collection, String document, Map<String, Calendar> field) {
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


}
