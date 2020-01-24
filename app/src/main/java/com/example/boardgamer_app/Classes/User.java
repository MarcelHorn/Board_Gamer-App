package com.example.boardgamer_app.Classes;

import java.util.Calendar;

public class User {

    private int id;

    private String name, mail, city, street, password;
    private Group group;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Calendar getInGroupSince() {
        return inGroupSince;
    }

    public void setInGroupSince(Calendar inGroupSince) {
        this.inGroupSince = inGroupSince;
    }
    //endregion


}
