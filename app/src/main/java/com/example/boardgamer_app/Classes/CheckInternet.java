package com.example.boardgamer_app.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {

    public static final String NO_CONNECTION = "Zum Fortfahren bitte Netzwerk prüfen!";

    public static boolean isNetwork(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    

//Vorlage zum Prüfen der Internetverbindung:

/*
                if (CheckInternet.isNetwork(Activity.this)) {
                    }
                else Toast.makeText(Activity.this, CheckInternet.NO_CONNECTION, Toast.LENGTH_SHORT).show();
*/

}