package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.example.boardgamer_app.Classes.DialogGames;
import com.example.boardgamer_app.Classes.DialogMessages;
import com.example.boardgamer_app.Classes.Game;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements DialogMessages.OnInputListener {


    private static final String TAG = "Main2Activity";

    String userName;
    Button buttonCreate;
    SimpleDateFormat sdf, sdfDetail;
    ListView listView;
    List<String> listViewObjects ;
    int deletePos;
    String deletedMessage;
    DatabaseController databaseController = new DatabaseController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        userName = getIntent().getStringExtra("UserName");

        buttonCreate = findViewById(R.id.btnCreateMessage);
        listView = findViewById(R.id.listViewMessages);
        listViewObjects = new ArrayList<>();
        sdf = new SimpleDateFormat("EEE dd.MM - HH:mm");
        sdfDetail = new SimpleDateFormat("EEE dd.MM - HH:mm:ss");

        buttonCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                DialogMessages dialog = new DialogMessages();
                dialog.show(getSupportFragmentManager(), "CustomMessages");
            }
        });

        databaseController.db
                .collection("Nachrichten")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshots : task.getResult()) {
                                String message = snapshots.getString("Message");
                                if (message != null)
                                listViewObjects.add(message);
                            }
                            LoadMessages();
                        }
                    }
                });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                deletePos = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setCancelable(true);
                builder.setTitle("Warnung");
                builder.setMessage("Wirklich diese Nachricht löschen?");
                builder.setPositiveButton("Bestätigen",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deletedMessage = listViewObjects.get(deletePos).trim();
                                databaseController.db.collection("Nachrichten")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot snapshots : task.getResult()) {
                                                        String mes = snapshots.getString("Message").trim();
                                                        if (mes.equals(deletedMessage)) {
                                                            listViewObjects.remove(deletedMessage);
                                                            snapshots.getReference().delete();
                                                            LoadMessages();
                                                        }
                                                    }
                                                }
                                            }
                                        });

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();



                Log.v("long clicked","pos: " + pos);

                return true;
            }
        });
    }


    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: got the Input");
        Toast.makeText(Main2Activity.this,input,Toast.LENGTH_SHORT).show();
        Date date = new Date();
        String time = sdfDetail.format(date);
        String newMessage = time + " - " + userName + ": " + input;
        listViewObjects.add (newMessage);
        LoadMessages();
        Map<String, Object> data = new HashMap<>();
        data.put("Message", newMessage);
        databaseController.writeInDatabase("Nachrichten", (userName + time), data);
        //newGame.setName(input);


    }

    private void LoadMessages() {
        listView.setAdapter(new ArrayAdapter<String>(
                Main2Activity.this,
                android.R.layout.simple_list_item_1,
                listViewObjects)
        );
    }
}
