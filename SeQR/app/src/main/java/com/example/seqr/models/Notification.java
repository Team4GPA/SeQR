package com.example.seqr.models;

import com.google.firebase.Timestamp;

import java.util.Map;

/**
 * A mode class that represents a notification with details such as time, sender, and description.
 */
public class Notification {
    private Timestamp time;
    private Profile sender;
    private String description;
    private Map<String, Object> notif;

    /**
     * Constructs a notification object with the provided timestamp, sender profile, and description.
     *
     * @param time        The timestamp indicating when the notification was created.
     * @param sender      The profile of the sender of the notification.
     * @param description The description or content of the notification.
     */
    public Notification(Timestamp time, Profile sender, String description) {
        this.time = time;
        this.sender = sender;
        this.description = description;
        //puts the notification into a map format
        notif.put("description",description);
        notif.put("sender", sender);
        notif.put("time", time);

    }

    /**
     * Retrieves the notification details as a map.
     *
     * @return A map containing the notification details.
     */
    public Map<String, Object> getNotif() {
        return notif;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNotif(Map<String, Object> notif) {
        this.notif = notif;
    }
}
