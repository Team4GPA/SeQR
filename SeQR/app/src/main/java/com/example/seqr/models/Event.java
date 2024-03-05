package com.example.seqr.models;

import android.app.Notification;


import java.util.List;
import com.google.firebase.Timestamp;

public class Event {
    private String eventName;
    private String eventPoster;
    private int checkInCount;
    private String eventDesc;
    private Timestamp eventStartTime;
    private int maxCapacity;
    private QR qrCode;
    private List<Profile> attendees;
    private List<Profile> signedUp;
    private Profile organizer;

    public Event(String eventName, String eventPoster, String eventDesc, int maxCapacity, QR qrCode, Profile organizer) {
        this.eventName = eventName;
        this.eventPoster = eventPoster;
        this.eventDesc = eventDesc;
        this.maxCapacity = maxCapacity;
        this.qrCode = qrCode;
        this.organizer = organizer;
    }

    // Need to add getters/setters


}
