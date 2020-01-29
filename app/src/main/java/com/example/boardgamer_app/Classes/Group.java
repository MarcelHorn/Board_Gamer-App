package com.example.boardgamer_app.Classes;

public class Group {
    private User[] member;
    private User admin;
    private Evening[] evenings;

    //Leerer Konstruktor für Firebase
    public Group() {
    }


    //region Getter und Setter
    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
    //endregion

}
