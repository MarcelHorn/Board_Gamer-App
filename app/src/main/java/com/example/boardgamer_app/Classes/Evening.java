package com.example.boardgamer_app.Classes;

import java.util.Calendar;

public class Evening {
    private User currentHost;
    private Calendar date;  //Tag und Uhrzeit
    private short ratingStars;
    private String ratingDescription;
    private boolean isFinished = false; //wird true, wenn Termin in Vergangenheit liegt (Bewertung)
    private Group group;

    //region Getter und Setter
    public short getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(short ratingStars) {
        this.ratingStars = ratingStars;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public User getCurrentHost() {
        return currentHost;
    }

    public void setCurrentHost(User currentHost) {
        this.currentHost = currentHost;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    //endregion

}
