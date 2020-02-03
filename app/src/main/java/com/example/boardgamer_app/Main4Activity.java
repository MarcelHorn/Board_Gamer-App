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
import android.widget.Toast;

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
    int weekday, interval;
    Calendar firstDate;
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


              if (!(selectedItem.equals("Wochentag wählen...") ))
               {
                    CalculateFirstEvening();
               }


            }
            //muss man schreiben, falls das Feld mal leer ist...
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void CalculateInterval() {
        Toast.makeText(Main4Activity.this, spinnerInterval.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        switch (spinnerInterval.getSelectedItem().toString())
        {
            case "7 Tage":
                interval = 7;
                break;
            case "14 Tage":
                interval = 14;
                break;
            case "28 Tage":
                interval = 21;
                break;
            default:
                interval = 0;
                break;
        }
    }

    //Methode zum Berrechnen des nächsten Wochentages in der Zukunft
    public Calendar CalculateFirstEvening()
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
        firstDate = calendar;
        CalculateInterval();

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
        return firstDate;
    }

    public void onClickRefreshGroup(View view) {


        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        String time = sdfTime.format(calendar.getTime());
        CalculateInterval();

        Map<String, Object> dataGroup = new HashMap<>();
        dataGroup.put("Wochentag" , calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        dataGroup.put("Rhythmus", interval);
        dataGroup.put("Uhrzeit", time);

        databaseController.writeInDatabase("Gruppe", "Gruppeneinstellungen", dataGroup);



        Calendar termin2 = (Calendar) firstDate.clone();
        termin2.add(Calendar.DATE, interval);

        Calendar termin3 = (Calendar) termin2.clone();
        termin3.add(Calendar.DATE, interval);

        Calendar termin4 = (Calendar) termin3.clone();
        termin4.add(Calendar.DATE, interval);

        Calendar termin5 = (Calendar) termin4.clone();
        termin5.add(Calendar.DATE, interval);



        Map<String, Calendar> dataEvenings = new HashMap<>();
        dataEvenings.put("Termin1", firstDate);
        dataEvenings.put("Termin2", termin2);
        dataEvenings.put("Termin3", termin3);
        dataEvenings.put("Termin4", termin4);
        dataEvenings.put("Termin5", termin5);




        databaseController.writeInDatabaseAsCalendar("Termine", "Anstehende Termine", dataEvenings);



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

