package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.boardgamer_app.Classes.DatabaseController;
import com.example.boardgamer_app.Classes.DialogMessages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_messages extends AppCompatActivity implements DialogMessages.OnInputListener {


    private static final String TAG = "Main2Activity";

    String userName;
    Button buttonCreate;
    SimpleDateFormat sdfShort, sdfDetail;
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
        sdfShort = new SimpleDateFormat("MM.dd-HH:mm:ss");
        sdfDetail = new SimpleDateFormat("EEE dd.MM - HH:mm:ss");

        buttonCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                DialogMessages dialog = new DialogMessages();
                dialog.show(getSupportFragmentManager(), "CustomMessages");
            }
        });

        //Holt alle Nachrichten aus der DB und packt sie in die Liste "listViewObjects"
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

        //Die Lösch Funktion beim langen drücken auf Nachricht
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                deletePos = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_messages.this);
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
                                                    LoadMessages();
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
                return true;
            }
        });
    }


    //Interface-Methode, analog wie die Spielevorschläge
    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: got the Input");
        Toast.makeText(Activity_messages.this,input,Toast.LENGTH_SHORT).show();
        Date date = new Date();
        String time = sdfDetail.format(date);
        String timeShort = sdfShort.format(date);
        String newMessage = time + " - " + userName + ": " + input;
        listViewObjects.add (newMessage);
        LoadMessages();
        Map<String, Object> data = new HashMap<>();
        data.put("Message", newMessage);
        databaseController.writeInDatabase("Nachrichten", (timeShort + userName), data);


    }

    private void LoadMessages() {
        listView.setAdapter(new ArrayAdapter<String>(
                Activity_messages.this,
                android.R.layout.simple_list_item_1,
                listViewObjects)
        );
    }
}
