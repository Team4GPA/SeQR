package com.example.seqr.models;

import com.google.firebase.Timestamp;

/**
 * A model class representing an announcement.
 */
public class Announcement {
    private String title;
    private String description;
    private Timestamp time;

    /**
     * Constructs an Announcement object with the given title, description, and time.
     *
     * @param title       The title of the announcement.
     * @param description The description of the announcement.
     * @param time        The timestamp indicating when the announcement was made.
     */
    public Announcement(String title, String description, Timestamp time){
        this.title = title;
        this.description = description;
        this.time = time;
    }

    /**
     * Retrieves the title of the announcement.
     *
     * @return The title of the announcement.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the announcement.
     *
     * @param title The title of the announcement.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the description of the announcement.
     *
     * @return The description of the announcement.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the announcement.
     *
     * @param description The description of the announcement.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the timestamp of the announcement.
     *
     * @return The timestamp indicating when the announcement was made.
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * Sets the timestamp of the announcement.
     *
     * @param time The timestamp indicating when the announcement was made.
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }
}
