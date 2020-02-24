package com.example.boardgamer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.boardgamer_app.MainActivity.KEY_NAME;

public class Activity_settings_user extends AppCompatActivity {

    private static final String TAG = "Main6Activity";



    EditText name, id;
    //gesamte Datenbank Instanz
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //FireStore kategorisiert in folgener Reihenfolge: Collection(Sammlung) > Document(document) > Feld
    //userRef weißt also auf User>"Email des aktuellen Nutzers"
    DocumentReference userRef = db.collection("User")
            .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);

        name = findViewById(R.id.editText_setting_name);
        id = findViewById(R.id.editTextId);

        //hiermit holt man sich das document
         userRef.get()
                 //bei erfolgreichen Laden
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Toast.makeText(Activity_settings_user.this,"Daten geladen", Toast.LENGTH_LONG).show();
                        if( documentSnapshot.exists())
                        {
                            String username = documentSnapshot.getString(KEY_NAME);
                            int i = documentSnapshot.getLong("id").intValue();
                            name.setText(username);
                            id.setText("" + i);
                        }
                        else
                        {
                            Toast.makeText(Activity_settings_user.this,"Daten nicht vorhanden", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                 //bei Ladefehler Exception
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });



    }
}
