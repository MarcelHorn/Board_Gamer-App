package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.boardgamer_app.Classes.TimePickerFragment;

public class Activity_create_group extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    //
    public void onClickTimeSelect(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
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
