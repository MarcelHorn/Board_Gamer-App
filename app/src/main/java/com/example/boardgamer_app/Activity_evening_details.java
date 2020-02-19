package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.example.boardgamer_app.Classes.DialogGames;
import com.example.boardgamer_app.Classes.Game;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.model.Document;


import java.lang.reflect.Array;
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
    Integer eveningId;
    TextView txtOrganizer, txtTime;
    Button mGameCreate;
    ListView mListView;
    String game, organizerName;
    int likes, userId;

    boolean userLikedGame[] = new boolean[10];
    boolean gameAlreadyExist;
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
        organizerName = getIntent().getStringExtra("Organizer");
        eveningId = getIntent().getIntExtra("id", 0);
        Log.d(TAG, "onCreate: " + eveningId);


        //Laden der User Id aus der Datenbank
        databaseController.db
                .collection(DatabaseController.USER_COL)
                .document(databaseController.mFirebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot)
                    {
                        userId = documentSnapshot.getLong("id").intValue();

                        //Laden der Spielevorschläge, NACHDEM die User Id geladen wurde
                        databaseController.db
                                .collection(DatabaseController.EVENING_COL)
                                .document("Termin"+eveningId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                                {
                                    @NonNull
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot)
                                    {
                                        if (documentSnapshot.exists()) {

                                            for (int i = 1; i != 10; i++)
                                            {
                                                String gameName = documentSnapshot.getString("Game" + i);
                                                String likes = documentSnapshot.getString("Game"+i+"Likes");
                                                Boolean userHasLiked = documentSnapshot.getBoolean("Game" + i + "User" + userId);

                                                if (userHasLiked == null && gameName != null) userHasLiked = false;
                                                if (gameName != null && likes != null)
                                                {
                                                    //Neues Objekt von Typ Game erstellen und die geladenen Daten setzen
                                                    Game newGame = new Game();
                                                    newGame.setName(gameName);
                                                    newGame.setLikes(Integer.parseInt(likes.trim()));
                                                    //das Objekt besitzt nur den Boolean des aktuellen Users, ob er geliket hat oder nicht
                                                    newGame.setUserId(userHasLiked);
                                                    //hinzufügen zu den anzuzeigenen Objekten in der ViewList
                                                    listViewObjects.add(newGame);
                                                }
                                            }
                                            //Methode, die die Liste erstellt und ggf. einfärbt bei gelikten Spielen
                                            LoadGames();
                                        }
                                    }
                                });
                    }
                });

        //setzten des Datums und des Veranstallters
        Date date = new Date(longTime);
        SimpleDateFormat sdfTime = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm");
        String time = sdfTime.format(date);

        txtTime.setText("Termin: " + time);
        txtOrganizer.setText("Veranstalter: " + organizerName);


        mGameCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogGames dialog = new DialogGames();
                dialog.show(getSupportFragmentManager(), "CustomGames");
            }
        });

        //OnItemClickListener für die Spiele, beim Klick wird entweder markiert oder zurückgenommen, die Likes werden um 1 erhöht/gesenkt
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game o = (Game) mListView.getItemAtPosition(position);
                if (o.isUserId()) {
                    o.setUserId(false);
                    o.setLikes(o.getLikes() - 1);
                } else{
                    o.setUserId(true);
                    o.setLikes(o.getLikes() + 1);
                }
                LoadGames();
            }
        });
    }

    //Methode wird beim Verlassen der Activity aufgerufen (Sichern der Daten und schreiben in DB)
    @Override
    protected void onStop() {
        super.onStop();
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < listViewObjects.size(); i++) {
            data.put("Game"+ (i+1), listViewObjects.get(i).getName());
            data.put("Game"+ (i+1)+"Likes", String.valueOf(listViewObjects.get(i).getLikes()));
            data.put("Game"+ (i+1)+"User" + userId, listViewObjects.get(i).isUserId());
        }

        databaseController.writeInDatabase(DatabaseController.EVENING_COL, "Termin" + eveningId, data);

    }

    public void LoadGames() {
        mListView.setAdapter(new ArrayAdapter<Game>(
                Activity_evening_details.this,
                android.R.layout.simple_list_item_1,
                listViewObjects) {
            //Überladen der getView Methode vom ArrayAdapter, zum Farbe ändern
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);

                if (listViewObjects.get(position).isUserId()) {
                    row.setBackgroundColor(Color.LTGRAY);
                } else {
                    row.setBackgroundColor(Color.WHITE);
                }
                return row;
            }
        });
    }

    //Interface Methode erhählt den namen des Spiels aus der Dialog Box und
    //erstellt damit ein neues Game-Objekt mit 0 likes und aktualisiert die List View
    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: got the Input");
        gameAlreadyExist = false;
        for (Game game : listViewObjects) {
            if (game.getName().equals(input)) {
                gameAlreadyExist = true;
            }
        }
        if (!gameAlreadyExist) {
             Game newGame = new Game();
             newGame.setName(input);
             newGame.setLikes(0);
             listViewObjects.add(newGame);
             LoadGames();
         } else {
             Toast.makeText(Activity_evening_details.this, "Spiel wird bereits vorgeschlagen!" , Toast.LENGTH_SHORT).show();
         }

    }
}
