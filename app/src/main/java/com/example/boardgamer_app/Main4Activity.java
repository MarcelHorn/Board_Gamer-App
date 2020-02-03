package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.example.boardgamer_app.Classes.GroupProperties;
import com.example.boardgamer_app.Classes.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Main4Activity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    //DatenbankController: Beinhaltet die FirebaseAuth und FireStore Instanz und diverse Methoden zum schreiben und Lesen
    DatabaseController databaseController = new DatabaseController();
    String weekday;
    Calendar calendar = Calendar.getInstance();
    Spinner spinnerWeekdays, spinnerInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_group);



        spinnerWeekdays = (Spinner) findViewById(R.id.spinnerWeekdays);
        spinnerInterval = (Spinner) findViewById(R.id.spinnerInterval);

        //Wochentag Adapter
        ArrayAdapter<String> adapterWeekdays = new ArrayAdapter<String>(Main4Activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.weekdays));
        adapterWeekdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeekdays.setAdapter(adapterWeekdays);

        //Intervall Adapter
        ArrayAdapter<String> adapterInterval = new ArrayAdapter<String>(Main4Activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.intervals));
        adapterWeekdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterval.setAdapter(adapterInterval);

        //Listener zum Abfangen beim auswählen eines Wochentages
        spinnerWeekdays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();

                //Bei "Wochentag wählen..." soll nichts passieren
                //Andernfalls wird das aktuelle Datum bestimmt mit .getInstance()
                if (!(selectedItem.equals("Wochentag wählen...") || selectedItem.equals("Rhythmus wählen...")))
                {
                    int weekday;
                    //Jeder Wochentag hat einen Integer wert in der "CALENDAR" Klasse, die werden hier gesetzt:
                    switch (selectedItem)
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

                    CalculateFirstEvening(weekday);
                }
            }
            //muss man schreiben, falls das Feld mal leer ist...
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    //Methode zum Berrechnen des nächsten Wochentages in der Zukunft
    public void CalculateFirstEvening(int weekday)
    {
        Calendar firstDate = calendar;

        //Solange 1 Tag draufrechnen, bis der geforderte Wochentag erreicht ist
        while (firstDate.get(Calendar.DAY_OF_WEEK) != weekday)
        {
            firstDate.add(Calendar.DATE, 1);
        }

        TextView textFirstEvening = (TextView) findViewById(R.id.textFirstEvening);
        //Format ändern auf dd.MM.yyyy zur Übersicht
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd.MM.yyyy");
        String formattedDate = df.format(firstDate.getTime());
        ////
        textFirstEvening.setText("Erster Spieleabend findet am "  + formattedDate + " statt.");

        //TODO: Uhrzeit muss berücksichtigt werden, damit Termine nicht in der Vergangenheit liegen(gleicher Tag)
        //Toast.makeText(Activity_create_group.this,date.getTime().toString(),Toast.LENGTH_LONG ).show();
    }

    public void onClickRefreshGroup(View view) {


        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        String time = sdfTime.format(calendar.getTime());

        Map<String, Object> dataGroup = new HashMap<>();
        dataGroup.put("Wochentag" , calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        dataGroup.put("Rhythmus", spinnerInterval.getSelectedItem().toString());
        dataGroup.put("Uhrzeit", time);

        databaseController.writeInDatabase("Gruppe", "Gruppeneinstellungen", dataGroup);

        





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

        Button button =findViewById(R.id.btnTime);
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

