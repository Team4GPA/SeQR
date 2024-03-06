package com.example.seqr.models;

import com.google.firebase.Timestamp;
public class Announcement {
    private String title;
    private String description;
    private Timestamp time;

    public Announcement(String title, String description, Timestamp time){
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
