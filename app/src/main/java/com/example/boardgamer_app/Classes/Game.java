package com.example.boardgamer_app.Classes;

public class Game {
    private String name;

    public boolean isUserId() {
        return UserId;
    }

    public void setUserId(boolean userId) {
        UserId = userId;
    }

    private boolean UserId;

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

    private int likes;

    public String toString()
    {
        return name + ", Stimmen: " + likes;
    }
}
