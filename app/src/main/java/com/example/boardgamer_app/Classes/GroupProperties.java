package com.example.boardgamer_app.Classes;

import java.util.Calendar;

public class GroupProperties {

    private String[] adminDocRef;
    private int intervall;
    private Calendar time;

    //Leerer Konstruktor für Firebase
    public GroupProperties() {
    }


    //region Getter und Setter
    public String[] getAdminRef() {
        return adminDocRef;
    }

    public void setAdmin(String[] admin) {
        this.adminDocRef = admin;
    }
    //endregion

}
