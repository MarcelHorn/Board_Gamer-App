package com.example.boardgamer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.google.firebase.Timestamp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_evening_details extends AppCompatActivity {


    Integer organizerId;
    TextView txtOrganizer, txtTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evening_details);

        txtTime = findViewById(R.id.textViewDetailsTime);
        txtOrganizer = findViewById(R.id.textViewDetailsOrganizer);

        Long longTime = getIntent().getLongExtra("Timestamp", 0);
        organizerId = getIntent().getIntExtra("Organizer", 0);

        Date date = new Date(longTime);
        SimpleDateFormat sdfTime = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm");
        String time = sdfTime.format(date);

        txtTime.setText("Termin: " + time);
        txtOrganizer.setText("Veranstallter: " + organizerId.toString());


    }
}
