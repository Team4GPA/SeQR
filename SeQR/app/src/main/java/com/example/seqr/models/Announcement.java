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


}
