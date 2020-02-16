package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_past_evening_details extends AppCompatActivity {

    TextView txtTime, txtOrganizer;
    String organizerName;
    Long longTime;
    Integer eveningId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_evening_details);

        txtTime = findViewById(R.id.textPastTime);
        txtOrganizer = findViewById(R.id.textPastOrganizer);

        longTime = getIntent().getLongExtra("Timestamp", 0);
        organizerName = getIntent().getStringExtra("Organizer");
        eveningId = getIntent().getIntExtra("Id", 0);

        //setzten des Datums und des Veranstallters
        Date date = new Date(longTime);
        SimpleDateFormat sdfTime = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm");
        String time = sdfTime.format(date);

        txtTime.setText("Termin: " + time);
        txtOrganizer.setText("Veranstallter: " + organizerName);
    }
}
