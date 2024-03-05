package com.example.seqr.models;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Notification {
    private Timestamp time;
    private Profile sender;
    private String description;
    private Map<String, Object> notif;

    public Notification(Timestamp time, Profile sender, String description) {
        this.time = time;
        this.sender = sender;
        this.description = description;
        //puts the notification into a map format
        notif.put("description",description);
        notif.put("sender", sender);
        notif.put("time", time);

    }

    public Map<String, Object> getNotif() {
        return notif;
    }
}
