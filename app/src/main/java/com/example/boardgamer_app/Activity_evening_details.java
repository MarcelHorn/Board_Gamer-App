package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.example.boardgamer_app.Classes.DialogGames;
import com.example.boardgamer_app.Classes.Game;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_evening_details extends AppCompatActivity implements DialogGames.OnInputListener {


    private static final String TAG = "Activity_evening_detail";

    DatabaseController databaseController = new DatabaseController();
    Integer organizerId, eveningId;
    TextView txtOrganizer, txtTime;
    Button mGameCreate;
    ListView mListView;
    String game;
    int likes;
    public List<Game> listViewObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evening_details);

        txtTime = findViewById(R.id.textViewDetailsTime);
        txtOrganizer = findViewById(R.id.textViewDetailsOrganizer);
        mGameCreate = findViewById(R.id.btnCreateGame);
        mListView = findViewById(R.id.listViewGames);

        listViewObjects = new ArrayList<Game>();

        Long longTime = getIntent().getLongExtra("Timestamp", 0);
        organizerId = getIntent().getIntExtra("Organizer", 0);
        eveningId = getIntent().getIntExtra("Id", 0);

        databaseController.db
                .collection("Spielevorschläge")
                .document("Termin"+eveningId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @NonNull
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                                for (int i = 1; i != 10; i++) {
                                    String gameName = documentSnapshot.getString("Game" + i);
                                    String likes = documentSnapshot.getString("Game"+i+"Likes");

                                    if (gameName != null && likes != null) {
                                        Game newGame = new Game();
                                        newGame.setName(gameName);
                                        newGame.setLikes(Integer.parseInt(likes.trim()));
                                        listViewObjects.add(newGame);
                                    }
                                }
                            LoadGames();
                        }

                    }
                });


        Date date = new Date(longTime);
        SimpleDateFormat sdfTime = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm");
        String time = sdfTime.format(date);

        txtTime.setText("Termin: " + time);
        txtOrganizer.setText("Veranstallter: " + organizerId.toString());

        mGameCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogGames dialog = new DialogGames();
                dialog.show(getSupportFragmentManager(), "CustomGames");
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < listViewObjects.size(); i++) {
            data.put("Game"+ (i+1), listViewObjects.get(i).getName());
            data.put("Game"+ (i+1)+"Likes", String.valueOf(listViewObjects.get(i).getLikes()));
        }
        databaseController.writeInDatabase("Spielevorschläge","Termin"+ eveningId , data);
    }

    public void LoadGames() {
        mListView.setAdapter(new ArrayAdapter<Game>(
                Activity_evening_details.this,
                android.R.layout.simple_list_item_1,
                listViewObjects
        ));
    }

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: got the Input");
        Game newGame = new Game();
        newGame.setName(input);
        newGame.setLikes(0);
        listViewObjects.add(newGame);
        LoadGames();

    }
}
