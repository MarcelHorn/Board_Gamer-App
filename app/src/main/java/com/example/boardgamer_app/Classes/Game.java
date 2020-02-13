package com.example.boardgamer_app.Classes;

public class Game {

    private String name;
    private boolean UserId;
    private int likes;

    //region Getter und Setter

    public boolean isUserId() {
        return UserId;
    }

    public void setUserId(boolean userId) {
        UserId = userId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }



    public String toString()
    {
        return name + ", Stimmen: " + likes;
    }
    //endregion

}
