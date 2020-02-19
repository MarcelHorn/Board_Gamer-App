package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.example.boardgamer_app.Classes.Evening;
import com.example.boardgamer_app.Classes.TimePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Activity_settings_evenings extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    //Interface Callback Funktion
    private interface FirestoreCallback {
        void onCallback(List<Integer> userIdList);
    }

    //DatenbankController: Beinhaltet die FirebaseAuth und FireStore Instanz und diverse Methoden zum schreiben und Lesen
    DatabaseController databaseController = new DatabaseController();


    int weekday, interval, intervalIndex, lastOrganizer, lastEveningIndex, memberCount;
    Calendar firstDate;
    Calendar calendar;
    CollectionReference userCollection;
    Spinner spinnerWeekdays, spinnerInterval;
    Button button;
    Evening evening;
    Timestamp time;
    ArrayList<Integer> userIdList = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_group);

        //Init von verschiedenen Variablen
        calendar = Calendar.getInstance();
        userCollection = databaseController.db.collection(DatabaseController.USER_COL);
        CollectionReference userCollection = databaseController.db.collection(DatabaseController.USER_COL);

        //Evening Objekt
        evening = new Evening();

        //
        spinnerWeekdays = (Spinner) findViewById(R.id.spinnerWeekdays);
        spinnerInterval = (Spinner) findViewById(R.id.spinnerInterval);
        button = findViewById(R.id.btnTime);

        //Wochentag Adapter
        ArrayAdapter<String> adapterWeekdays = new ArrayAdapter<String>(Activity_settings_evenings.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.weekdays));
        adapterWeekdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeekdays.setAdapter(adapterWeekdays);

        //Intervall Adapter
        ArrayAdapter<String> adapterInterval = new ArrayAdapter<String>(Activity_settings_evenings.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.intervals));
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterval.setAdapter(adapterInterval);

        //Listener zum Abfangen beim auswählen eines Wochentages
        spinnerWeekdays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();

            }
            //muss man schreiben, falls das Feld mal leer ist...
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Laden der Daten aus "Gruppeneinstellungen" und Anzeigen in den einzelnen Komponenten
        databaseController.db.collection(DatabaseController.GROUP_COL)
                .document(DatabaseController.GROUP_SETTINGS_DOC)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            int interval = documentSnapshot.getLong("RhythmusIndex").intValue();
                            time = documentSnapshot.getTimestamp("Uhrzeit");
                            int weekday = documentSnapshot.getLong("Wochentag").intValue();
                            memberCount = documentSnapshot.getLong("AnzahlMitgliederIndex").intValue();
                            lastOrganizer = documentSnapshot.getLong("lastOrganizer").intValue();

                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                            spinnerWeekdays.setSelection(weekday - 1);
                            spinnerInterval.setSelection(interval);
                            button.setText(sdf.format(time.toDate()) + " Uhr");
                        }
                    }
                });
    }

    private void CalculateInterval() {
        //Toast.makeText(Main4Activity.this, spinnerInterval.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        switch (spinnerInterval.getSelectedItem().toString())
        {
            case "7 Tage":
                interval = 7;
                intervalIndex = 0;
                break;
            case "14 Tage":
                interval = 14;
                intervalIndex = 1;
                break;
            case "28 Tage":
                interval = 21;
                intervalIndex = 2;
                break;
            default:
                interval = 0;
                intervalIndex = 0;
                break;
        }
    }

    //Methode zum Berrechnen des nächsten Wochentages in der Zukunft
    public void CalculateFirstEvening()
    {
        //Toast.makeText(Main4Activity.this, spinnerWeekdays.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        switch (spinnerWeekdays.getSelectedItem().toString())
        {
            case "Montags":
                weekday = 2;
                break;
            case "Dienstags":
                weekday = 3;
                break;
            case "Mittwochs":
                weekday = 4;
                break;
            case "Donnerstags":
                weekday = 5;
                break;
            case "Freitags":
                weekday = 6;
                break;
            case "Samstags":
                weekday = 7;
                break;
            case "Sonntags":
                weekday = 1;
                break;
            default:
                weekday = 0;
                break;
        }

        firstDate = (Calendar) calendar.clone();


        //Solange 1 Tag draufrechnen, bis der geforderte Wochentag erreicht ist
        while (firstDate.get(Calendar.DAY_OF_WEEK) != weekday)
        {
            firstDate.add(Calendar.DATE, 1);
        }

        //TextView textFirstEvening = (TextView) findViewById(R.id.textFirstEvening);
        //Format ändern auf dd.MM.yyyy zur Übersicht
        //SimpleDateFormat df = new SimpleDateFormat("EEEE dd.MM.yyyy");
        //String formattedDate = df.format(firstDate.getTime());
        ////
        //textFirstEvening.setText("Erster Spieleabend findet am "  + formattedDate + " statt.");

        //TODO: Uhrzeit muss berücksichtigt werden, damit Termine nicht in der Vergangenheit liegen(gleicher Tag)
        //Toast.makeText(Activity_create_group.this,date.getTime().toString(),Toast.LENGTH_LONG ).show();

    }

    public void onClickRefreshGroup(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_settings_evenings.this);
        builder.setCancelable(true);
        builder.setTitle("Warnung");
        builder.setMessage("Wirklich die Gruppeneinstellungen aktualisieren? (Daten gehen möglicherweise verloren)");
        builder.setPositiveButton("Bestätigen",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteOldEvenings();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();




    }//Ende des Aktualisierungs-Buttons


    private void DeleteOldEvenings() {
        databaseController.db.collection(DatabaseController.EVENING_COL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                snapshot.getReference().delete();
                            }
                            RefreshGroup();
                        }
                    }
                });
    }

    private void RefreshGroup() {
        lastEveningIndex = 1;

        CalculateInterval();
        CalculateFirstEvening();

        Map<String, Object> dataGroup = new HashMap<>();
        dataGroup.put("Wochentag" , weekday);
        dataGroup.put("Rhythmus", firstDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        dataGroup.put("RhythmusIndex", intervalIndex);
        dataGroup.put("Uhrzeit", time);
        //dataGroup.put("AnzahlMitgliederIndex", 3);


        databaseController.UpdateDatabase(DatabaseController.GROUP_COL, DatabaseController.GROUP_SETTINGS_DOC, dataGroup);
        

        //Die Callback Funktion ist nötig, da das Laden aus der DB asynchron zum normalen Codeverlauf abläuft (Daten werden ausgelesen bevor sie geladen sind)
        //holt alle Id"s der Registrierten User und packt sie in eine Array-List
        ReadUserId(new FirestoreCallback() {
            @Override
            public void onCallback(List<Integer> userIdList) {

                //Berechnung der 5 Termine basierend am Rhythmus
                Timestamp timestamp = new Timestamp(firstDate.getTime());

                Calendar termin2 = (Calendar) firstDate.clone();
                termin2.add(Calendar.DATE, interval);
                Timestamp timestamp2 = new Timestamp(termin2.getTime());

                Calendar termin3 = (Calendar) termin2.clone();
                termin3.add(Calendar.DATE, interval);
                Timestamp timestamp3 = new Timestamp(termin3.getTime());

                Calendar termin4 = (Calendar) termin3.clone();
                termin4.add(Calendar.DATE, interval);
                Timestamp timestamp4 = new Timestamp(termin4.getTime());

                Calendar termin5 = (Calendar) termin4.clone();
                termin5.add(Calendar.DATE, interval);
                Timestamp timestamp5 = new Timestamp(termin5.getTime());

                //erster Termin

                evening.setDate(timestamp);
                evening.setEveningName("Termin"+lastEveningIndex);

                //for-Schleife die alle Id's der User anspricht
                for (int i = 0; i<userIdList.size(); i++)
                {
                    //Wenn der letze User den voherigen Termin abgehalten hat, fange von vorne an

                    if (lastOrganizer > memberCount) {
                        lastOrganizer = 1;
                    }
                    //
                    if (lastOrganizer == userIdList.get(i)) {
                        lastOrganizer += 1; //erhöhe um 1, damit im nächsten termin ein anderer Veranstallter gewählt wird
                        Map<String, Object> dataDate = new HashMap<>();
                        dataDate.put("Organizer", userIdList.get(i));
                        dataDate.put("Datum", evening.getDate());
                        dataDate.put("id", lastEveningIndex);
                        lastEveningIndex++;
                        databaseController.writeInDatabase(DatabaseController.EVENING_COL, evening.getEveningName(), dataDate);
                        //stoppt die for schleife, wenn der passende User gefunden wird
                        break;
                    }
                }

                //Zweiter termin
                evening.setDate(timestamp2);
                evening.setEveningName("Termin"+lastEveningIndex);
                for (int i = 0; i<userIdList.size(); i++)
                {
                    if (lastOrganizer > memberCount) {
                        lastOrganizer = 1;
                    }
                    if (lastOrganizer == userIdList.get(i)) {
                        lastOrganizer += 1;
                        Map<String, Object> dataDate2 = new HashMap<>();
                        dataDate2.put("Organizer", userIdList.get(i));
                        dataDate2.put("Datum", evening.getDate());
                        dataDate2.put("id", lastEveningIndex);
                        lastEveningIndex++;
                        databaseController.writeInDatabase(DatabaseController.EVENING_COL, evening.getEveningName(), dataDate2);
                        break;
                    }
                }

                //dritter Termin
                evening.setDate(timestamp3);
                evening.setEveningName("Termin"+lastEveningIndex);
                for (int i = 0; i<userIdList.size(); i++)
                {
                    if (lastOrganizer > memberCount) {
                        lastOrganizer = 1;
                    }
                    if (lastOrganizer == userIdList.get(i)) {
                        lastOrganizer += 1;
                        Map<String, Object> dataDate3 = new HashMap<>();
                        dataDate3.put("Organizer", userIdList.get(i));
                        dataDate3.put("Datum", evening.getDate());
                        dataDate3.put("id", lastEveningIndex);
                        lastEveningIndex++;
                        databaseController.writeInDatabase(DatabaseController.EVENING_COL, evening.getEveningName(), dataDate3);
                        break;
                    }
                }

                //vierter Termin
                evening.setDate(timestamp4);
                evening.setEveningName("Termin"+lastEveningIndex);
                for (int i = 0; i<userIdList.size(); i++)
                {
                    if (lastOrganizer > memberCount) {
                        lastOrganizer = 1;
                    }
                    if (lastOrganizer == userIdList.get(i)) {
                        lastOrganizer += 1;
                        Map<String, Object> dataDate4 = new HashMap<>();
                        dataDate4.put("Organizer", userIdList.get(i));
                        dataDate4.put("Datum", evening.getDate());
                        dataDate4.put("id", lastEveningIndex);
                        lastEveningIndex++;
                        databaseController.writeInDatabase(DatabaseController.EVENING_COL, evening.getEveningName(), dataDate4);
                        break;
                    }
                }

                //fünfter Termin
                evening.setDate(timestamp5);
                evening.setEveningName("Termin"+lastEveningIndex);
                for (int i = 0; i<userIdList.size(); i++)
                {
                    if (lastOrganizer > memberCount) {
                        lastOrganizer = 1;
                    }
                    if (lastOrganizer == userIdList.get(i)) {
                        lastOrganizer += 1;
                        Map<String, Object> dataDate5 = new HashMap<>();
                        dataDate5.put("Organizer", userIdList.get(i));
                        dataDate5.put("Datum", evening.getDate());
                        dataDate5.put("id", lastEveningIndex);
                        lastEveningIndex++;
                        databaseController.writeInDatabase(DatabaseController.EVENING_COL, evening.getEveningName(), dataDate5);
                        break;
                    }
                }
            }
        });


        //Updatet am Ende den "lastOrganizer", also der Veranstallter des 5.ten Termins, damit beim nächsten Terminupdate die Reihenfolge fortgesetzt wird
        Map<String, Object> lastOrganizerData = new HashMap<>();
        lastOrganizerData.put("lastOrganizer", lastOrganizer);
        databaseController.UpdateDatabase(DatabaseController.GROUP_COL,DatabaseController.GROUP_SETTINGS_DOC,lastOrganizerData);

        Toast.makeText(Activity_settings_evenings.this, "Erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();

    }
    //Methode zur Callback Funktion
    private void ReadUserId(final FirestoreCallback firestoreCallback) {
        userCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshots : task.getResult()) {
                                int id = snapshots.getLong("id").intValue();
                                userIdList.add(id);
                            }
                            firestoreCallback.onCallback(userIdList);
                        }
                    }
                });
    }


    public void onClickTimeSelect(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    //Überschreibt die Methode onTimeSet aus dem timePickerDialog-Interface
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {
        calendar.set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, i1);
        Date date = new Date();
        date = calendar.getTime();
        Timestamp newTs = new Timestamp(date);
        time = newTs;
        if (i1 == 0)
        {
            button.setText( i + ":00 Uhr");    //damit bei z.B. 12 Uhr 12:00 angezeigt wird, statt 12:0
        } else if (i1 < 10) {
            button.setText(i + ":0" + i1 + " Uhr");
        }else
            {
            button.setText( i + ":" + i1 + " Uhr");
        }
    }


    }

