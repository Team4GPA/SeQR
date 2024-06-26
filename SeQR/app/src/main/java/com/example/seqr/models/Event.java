package com.example.seqr.models;

import android.app.Notification;


import java.util.List;
import com.google.firebase.Timestamp;
import java.util.UUID;

/**
 * A model class representing an event.
 */
public class Event {
    private String eventName;
    private String eventDesc;
    private int maxCapacity;
    private String organizer;
    private String location;
    private Timestamp eventStartTime;
    private String eventID;
    private Timestamp createdTime;
    private String promotionQR;
    private String checkInQR;
    private String organizerUUID;
    private int milestoneAlert;
    private double latitude;
    private double longitude;

    /**
     * Default constructor for the Event class.
     */
    public Event(){

    }

    /**
     * Constructs an Event object with the specified parameters.
     *
     * @param eventName     The name of the event.
     * @param eventID       The unique ID of the event.
     * @param eventDesc     The description of the event.
     * @param maxCapacity   The maximum capacity of the event.
     * @param organizer     The organizer of the event.
     * @param location      The location of the event.
     * @param eventStartTime The start time of the event.
     * @param promotionQR   The QR code for promotion of the event.
     * @param checkInQR     The QR code for event check-in.
     * @param organizerUUID The UUID of the organizer.
     * @param createdTime   The time event was created.
     * @param longitude     A double representing the longitude coordinate of the location of the event
     * @param latitude      A double representing the latitude coordinate of the location of the event
     */
    public Event(String eventName, String eventID, String eventDesc,
                 int maxCapacity, String organizer, String location,
                 Timestamp eventStartTime, String promotionQR, String checkInQR,
                 String organizerUUID, Timestamp createdTime,
                 double latitude,
                 double longitude) {
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
        this.createdTime = createdTime;
        this.milestoneAlert = 0;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Retrieves the UUID of the event organizer.
     *
     * @return The UUID of the event organizer.
     */
    public String getOrganizerUUID() {
        return organizerUUID;
    }

    /**
     * Sets the UUID of the event organizer.
     *
     * @param organizerUUID The UUID of the event organizer.
     */
    public void setOrganizerUUID(String organizerUUID) {
        this.organizerUUID = organizerUUID;
    }

    /**
     * Retrieves the QR code for event promotion.
     *
     * @return The QR code for event promotion.
     */
    public String getPromotionQR() {
        return promotionQR;
    }

    /**
     * Sets the QR code for event promotion.
     *
     * @param promotionQR The QR code for event promotion.
     */
    public void setPromotionQR(String promotionQR) {
        this.promotionQR = promotionQR;
    }

    /**
     * Retrieves the QR code for event check-in.
     *
     * @return The QR code for event check-in.
     */
    public String getCheckInQR() {
        return checkInQR;
    }

    /**
     * Sets the QR code for event check-in.
     *
     * @param checkInQR The QR code for event check-in.
     */
    public void setCheckInQR(String checkInQR) {
        this.checkInQR = checkInQR;
    }

    /**
     * Sets the ID of the event.
     *
     * @param eventID The ID of the event.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Retrieves the ID of the event.
     *
     * @return The ID of the event.
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Retrieves the name of the event.
     *
     * @return The name of the event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event.
     *
     * @param eventName The name of the event.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    /**
     * Retrieves the description of the event.
     *
     * @return The description of the event.
     */
    public String getEventDesc() {
        return eventDesc;
    }

    /**
     * Sets the description of the event.
     *
     * @param eventDesc The description of the event.
     */
    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    /**
     * Retrieves the maximum capacity of the event.
     *
     * @return The maximum capacity of the event.
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Sets the maximum capacity of the event.
     *
     * @param maxCapacity The maximum capacity of the event.
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Retrieves the organizer of the event.
     *
     * @return The organizer of the event.
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * Sets the organizer of the event.
     *
     * @param organizer The organizer of the event.
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    /**
     * Retrieves the location of the event.
     *
     * @return The location of the event.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the event.
     *
     * @param location The location of the event.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the start time of the event.
     *
     * @return The start time of the event.
     */
    public Timestamp getEventStartTime() {
        return eventStartTime;
    }

    /**
     * Sets the start time of the event.
     *
     * @param eventStartTime The start time of the event.
     */
    public void setEventStartTime(Timestamp eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    /**
     * Gets the milestone Alert
     * @return an integer representing the milestone alert
     */
    public int getMilestoneAlert() {
        return milestoneAlert;
    }

    /**
     * Sets the milestone alert
     * @param milestoneAlert the milestone alert
     */
    public void setMilestoneAlert(int milestoneAlert) {
        this.milestoneAlert = milestoneAlert;
    }

    /**
     * gets the timestamp of when the event was made
     * @return when the event was created
     */
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    /**
     * sets the time the event was created
     * @param createdTime a timestamp of when the event was created
     */
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    /**
     *  gets the latitude of an event
     * @return double representing the latitude of the event
     */
    public double getLatitude(){return latitude;}

    /**
     * gets the longitude of the event
     * @return double representing the longitude of the event
     */
    public double getLongitude() {return longitude;}

    /**
     * sets the latitude of the event
     * @param lat the latitude
     */
    public void setLatitude(double lat) {this.latitude = lat;}

    /**
     * sets the longitude of the event
     * @param lng the longitude
     */
    public void setLongitude(double lng) {this.longitude = lng;}
}
