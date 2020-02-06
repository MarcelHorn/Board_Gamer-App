package com.example.boardgamer_app.Classes;


import com.google.firebase.Timestamp;

import java.util.Calendar;

public class Evening {

    private Timestamp date;
    private String EveningName;
    private int organizerId;

    public String getEveningName() {
        return EveningName;
    }

    public void setEveningName(String eveningName) {
        EveningName = eveningName;
    }





    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }



    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }



}
