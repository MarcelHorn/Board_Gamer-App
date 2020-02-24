package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity_past_evening_details extends AppCompatActivity {

    private static final String TAG = "Activity_past_evening_d";

    TextView txtTime, txtOrganizer;
    String organizerName;
    Long longTime;
    Integer eveningId;
    int  ratingUserCount, userId;
    float ratingOrganizerCount, ratingFoodCount, ratingGeneralCount;
    DecimalFormat df;

    RatingBar ratingOrganizer, ratingFood, ratingEvening;
    TextView textOrganizer, textFood, textEvening;
    Button buttonCreateRating;

    DatabaseController databaseController = new DatabaseController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_evening_details);

        //wird aus der past_evening activity geladen, die es aus der Main_activity hat (spart Ladevorgänge der DB)
        userId = getIntent().getIntExtra("UserNameId", 0);


        txtTime = findViewById(R.id.textPastTime);
        txtOrganizer = findViewById(R.id.textPastOrganizer);

        longTime = getIntent().getLongExtra("Timestamp", 0);
        organizerName = getIntent().getStringExtra("Organizer");
        eveningId = getIntent().getIntExtra("Id", 0);

        buttonCreateRating = findViewById(R.id.btnCreateRating);
        ratingOrganizer = findViewById(R.id.ratingBarOrganizer);
        textOrganizer = findViewById(R.id.textRatingOrganizer);
        ratingFood = findViewById(R.id.ratingBarFood);
        textFood = findViewById(R.id.textRatingFood);
        ratingEvening = findViewById(R.id.ratingBarGeneral);
        textEvening = findViewById(R.id.textRatingGeneral);


        //setzten des Datums und des Veranstalters
        Date date = new Date(longTime);
        SimpleDateFormat sdfTime = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm");
        String time = sdfTime.format(date);

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        txtTime.setText("Termin: " + time);
        txtOrganizer.setText("Veranstalter: " + organizerName);

        CheckUserRated();

        //Logik beim Klick auf Button:
        //1. Beim Klick des Buttons "Bewertung abgeben" prüfen, ob User bereits bewertet hat, sonst Sternebewertungen laden (Besser: Button sperren)
        //2. Prüfe, ob alle Werte != 0 sind, sonst Toast
        //3. Laden der in DB gespeicherten Einzelbewertungen und Anzahl bereits getätigter Bewertungen
        //4. Bewertung des Users der Einzelbewertungen hinzufügen und Anzahl getätigter Bewertungen erhöhen
        //5. Schreiben in DB der Gesamtbewertung, Anzahl Bewertungen und UserHatAbgestimmt
        buttonCreateRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ratingOrganizer.getRating() == 0 || ratingEvening.getRating() == 0 || ratingFood.getRating() == 0) {
                    Toast.makeText(Activity_past_evening_details.this, "Bitte alle Punkte bewerten", Toast.LENGTH_SHORT).show();
                } else {
                    databaseController.mDatabase
                            .collection(DatabaseController.PAST_EVENING_COL)
                            .document("Termin" + eveningId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        ratingOrganizerCount = task.getResult().getLong("BewertungVer").floatValue();
                                        ratingFoodCount = task.getResult().getLong("BewertungFood").floatValue();
                                        ratingGeneralCount = task.getResult().getLong("BewertungAllg").floatValue();

                                        ratingUserCount = task.getResult().getLong("AnzahlBewertungen").intValue();

                                        ratingOrganizerCount += ratingOrganizer.getRating();
                                        ratingFoodCount += ratingFood.getRating();
                                        ratingGeneralCount += ratingEvening.getRating();

                                        ratingUserCount++;

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("BewertungVer", ratingOrganizerCount);
                                        data.put("BewertungFood", ratingFoodCount);
                                        data.put("BewertungAllg", ratingGeneralCount);
                                        data.put("AnzahlBewertungen", ratingUserCount);
                                        data.put("User" + userId + "rated", true);

                                        databaseController.writeInDatabase(DatabaseController.PAST_EVENING_COL, "Termin" + eveningId, data);
                                        Toast.makeText(Activity_past_evening_details.this, "Vielen Dank für die Bewertung!", Toast.LENGTH_SHORT).show();
                                        CheckUserRated();
                                    }
                                }
                            });
                }

            }
        });

    }

    //Prüft, ob User bereits abgestimmt hat, wenn ja werden alle wählbaren Komponenten gesperrt und die Gesamtbewertung aus der DB geladen
    private void CheckUserRated() {
        databaseController.mDatabase.collection(DatabaseController.PAST_EVENING_COL)
                .document("Termin"+ eveningId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            if (task.getResult().getBoolean("User" + userId + "rated") != null) {
                                boolean hasUserRated = task.getResult().getBoolean("User" + userId + "rated");

                                if (hasUserRated) {
                                    ratingUserCount = task.getResult().getLong("AnzahlBewertungen").intValue();
                                    buttonCreateRating.setEnabled(false);
                                    buttonCreateRating.setText("Bereits abgestimmt! Stimmen gesamt: " + ratingUserCount);

                                    //Damit die angezeigten Wertungen Nachkommastellen haben hier float
                                    float organizer = task.getResult().getLong("BewertungVer").floatValue() / ratingUserCount;
                                    float food = task.getResult().getLong("BewertungFood").floatValue() / ratingUserCount;
                                    float general = task.getResult().getLong("BewertungAllg").floatValue() / ratingUserCount;

                                    ratingOrganizer.setRating(task.getResult().getLong("BewertungVer").intValue() / ratingUserCount);
                                    textOrganizer.setText("Gastgeber: Wertung: " + df.format(organizer));
                                    ratingFood.setRating(task.getResult().getLong("BewertungFood").intValue() / ratingUserCount);
                                    textFood.setText("Essen: Wertung: " + df.format(food));
                                    ratingEvening.setRating(task.getResult().getLong("BewertungAllg").intValue() / ratingUserCount);
                                    textEvening.setText("Abend allgemein: Wertung: " + df.format(general));

                                    ratingOrganizer.setEnabled(false);
                                    ratingFood.setEnabled(false);
                                    ratingEvening.setEnabled(false);
                                }
                            }
                        }
                    }
                });
    }
}
