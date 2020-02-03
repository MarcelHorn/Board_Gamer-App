package com.example.boardgamer_app.Classes;

import java.util.Calendar;

public class User {

    private int id;

    private String name;
    private String mail;

    //Datum, wann User die Gruppe beigetreten ist.
    private Calendar inGroupSince;


    //region Getter und Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Calendar getInGroupSince() {
        return inGroupSince;
    }

    public void setInGroupSince(Calendar inGroupSince) {
        this.inGroupSince = inGroupSince;
    }
    //endregion


}
