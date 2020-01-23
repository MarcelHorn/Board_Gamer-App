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

import com.example.boardgamer_app.Classes.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

public class Activity_create_group extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Spinner spinnerWeekdays = (Spinner) findViewById(R.id.spinnerWeekdays);
        Spinner spinnerInterval = (Spinner) findViewById(R.id.spinnerInterval);

        //Wochentag Adapter
        ArrayAdapter<String> adapterWeekdays = new ArrayAdapter<String>(Activity_create_group.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.weekdays));
        adapterWeekdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeekdays.setAdapter(adapterWeekdays);

        //Intervall Adapter
        ArrayAdapter<String> adapterInterval = new ArrayAdapter<String>(Activity_create_group.this,
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
                if(!selectedItem.equals("Wochentag wählen..."))
                {
                    Calendar date1 = Calendar.getInstance();
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

                    CalculateFirstEvening(date1, weekday);
                }
            }
            //muss man schreiben, falls das Feld mal leer ist...
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    //Methode zum Berrechnen des nächsten Wochentages in der Zukunft
    public void CalculateFirstEvening(Calendar date, int weekday)
    {
        //Solange 1 Tag draufrechnen, bis der geforderte Wochentag erreicht ist
        while (date.get(Calendar.DAY_OF_WEEK) != weekday)
        {
            date.add(Calendar.DATE, 1);
        }

        TextView textFirstEvening = (TextView) findViewById(R.id.textFirstEvening);
        //Format ändern auf dd.MM.yyyy zur Übersicht
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd.MM.yyyy");
        String formattedDate = df.format(date.getTime());
        ////
        textFirstEvening.setText("Erster Spieleabend findet am "  + formattedDate + " statt.");

        //TODO: Uhrzeit muss berücksichtigt werden, damit Termine nicht in der Vergangenheit liegen(gleicher Tag)
        //Toast.makeText(Activity_create_group.this,date.getTime().toString(),Toast.LENGTH_LONG ).show();
    }

    //
    public void onClickTimeSelect(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    public void onClickCreateGroup(View view) {
        Spinner spinnerWeekdays = (Spinner) findViewById(R.id.spinnerWeekdays);
        Spinner spinnerInterval = (Spinner) findViewById(R.id.spinnerInterval);
        Button time = (Button) findViewById(R.id.btnTime);
        String toast = "Gruppe erstellt: Uhrzeit: " + time.getText() + ", Wochentag: " + spinnerWeekdays.getSelectedItem().toString() + ", Rhythmus: "+ spinnerInterval.getSelectedItem().toString();
        Toast.makeText(Activity_create_group.this,toast,Toast.LENGTH_LONG ).show();


    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {
        Button button =findViewById(R.id.btnTime);
        if (i1 == 0)
        {
            button.setText( i + ":00 Uhr");    //damit bei z.B. 12 Uhr 12:00 angezeigt wird, statt 12:0
        } else
            {
                button.setText( i + ":" + i1 + " Uhr");
            }

    }
}
