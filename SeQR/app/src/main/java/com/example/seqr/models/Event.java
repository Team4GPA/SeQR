package com.example.seqr.models;

import android.app.Notification;


import java.util.List;
import com.google.firebase.Timestamp;
import java.util.UUID;

/**
 * This model class represents events
 */
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

    private String organizerUUID;

    public Event(){

    }

    public Event(String eventName, String eventID, String eventDesc, int maxCapacity, String organizer, String location, String eventStartTime, String promotionQR, String checkInQR, String organizerUUID) {
        this.eventName = eventName;

        this.eventDesc = eventDesc;
        this.maxCapacity = maxCapacity;
        this.organizer = organizer;
        this.location = location;
        this.eventStartTime = eventStartTime;
        this.eventID = eventID;
        this.checkInQR = checkInQR;
        this.promotionQR = promotionQR;
        this.organizerUUID = organizerUUID;

    }

    /**
     * getter for organizerUUID
     */
    public String getOrganizerUUID() {
        return organizerUUID;
    }

    /**
     * setter for organizerUUID
     */
    public void setOrganizerUUID(String organizerUUID) {
        this.organizerUUID = organizerUUID;
    }

    /**
     * getter for promotionQR
     */
    public String getPromotionQR() {
        return promotionQR;
    }

    /**
     * setter for promotionQR
     */
    public void setPromotionQR(String promotionQR) {
        this.promotionQR = promotionQR;
    }

    /**
     * getter for checkInQR
     */
    public String getCheckInQR() {
        return checkInQR;
    }

    /**
     * setter for checkInQR
     */
    public void setCheckInQR(String checkInQR) {
        this.checkInQR = checkInQR;
    }

    /**
     * setter for eventID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * getter for eventID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * getter for eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * getter for eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    /**
     * getter for eventDescription
     */
    public String getEventDesc() {
        return eventDesc;
    }

    /**
     * setter for eventDescription
     */
    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    /**
     * getter maxCapacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * setter maxCapacity
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * getter for organizer
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * setter for organizer
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    /**
     * getter for location
     */
    public String getLocation() {
        return location;
    }

    /**
     * setter for location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * getter for eventStartTime
     */
    public String getEventStartTime() {
        return eventStartTime;
    }

    /**
     * setter for eventStartTime
     */
    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
}
