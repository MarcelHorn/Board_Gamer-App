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

public class Main6Activity extends AppCompatActivity {

    private static final String TAG = "Main6Activity";
    EditText name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference userRef = db.collection("User")
            .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);

        name = findViewById(R.id.editText_setting_name);

         userRef.get()

                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Toast.makeText(Main6Activity.this,"Success", Toast.LENGTH_LONG).show();
                        if( documentSnapshot.exists())
                        {
                            String username = documentSnapshot.getString("name");
                            name.setText(username);
                        }
                        else
                        {
                            Toast.makeText(Main6Activity.this,"Daten nicht vorhanden", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });



    }
}
