package com.example.seqr.models;

import com.google.firebase.Timestamp;

/**
 * This model class represents Announcements
 */
public class Announcement {
    private String title;
    private String description;
    private Timestamp time;

    public Announcement(String title, String description, Timestamp time){
        this.title = title;
        this.description = description;
        this.time = time;
    }

    /**
     * getter for title
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter for description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getter for time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * setter for time
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }
}
