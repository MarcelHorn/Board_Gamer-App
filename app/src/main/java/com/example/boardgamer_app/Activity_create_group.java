package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.TimePickerFragment;

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
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Button button =findViewById(R.id.btnTime);
        if (i1 == 0) {
            button.setText( i + ":00 Uhr");    //damit bei z.B. 12 Uhr 12:00 angezeigt wird, statt 12:0
        } else {
            button.setText( i + ":" + i1 + " Uhr");
        }

    }
}
