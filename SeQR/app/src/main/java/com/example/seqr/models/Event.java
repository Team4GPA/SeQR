package com.example.seqr.models;

import android.app.Notification;


import java.util.List;
import com.google.firebase.Timestamp;
import java.util.UUID;

public class Event {
    private String eventName;

    private String eventDesc;
    private int maxCapacity;
    private String organizer;
    private String location;
    private String eventStartTime;
    private String eventID;

    private String promotionQR;

    private String checkInQR;

    public Event(){

    }


    public Event(String eventName, String eventID, String eventDesc, int maxCapacity, String organizer, String location, String eventStartTime, String promotionQR, String checkInQR) {
        this.eventName = eventName;

        this.eventDesc = eventDesc;
        this.maxCapacity = maxCapacity;
        this.organizer = organizer;
        this.location = location;
        this.eventStartTime = eventStartTime;
        this.eventID = eventID;
        this.checkInQR = checkInQR;
        this.promotionQR = promotionQR;

    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
}
