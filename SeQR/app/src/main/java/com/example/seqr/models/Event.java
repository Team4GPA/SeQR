package com.example.seqr.models;

import android.app.Notification;


import java.util.List;
import com.google.firebase.Timestamp;
import java.util.UUID;

public class Event {
    private String eventName;
    private String eventPoster;
    private String eventDesc;
    private int maxCapacity;
    private String organizer;
    private String location;
    private Timestamp eventStartTime;
    private String eventID;

    public Event(){

    }


    public Event(String eventName, String eventPoster, String eventDesc, int maxCapacity, String organizer, String location, Timestamp eventStartTime) {
        this.eventName = eventName;
        this.eventPoster = eventPoster;
        this.eventDesc = eventDesc;
        this.maxCapacity = maxCapacity;
        this.organizer = organizer;
        this.location = location;
        this.eventStartTime = eventStartTime;
        if(this.eventID == null){
            this.eventID = UUID.randomUUID().toString();
        }
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventPoster() {
        return eventPoster;
    }

    public void setEventPoster(String eventPoster) {
        this.eventPoster = eventPoster;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Timestamp eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
}
