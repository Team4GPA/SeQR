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

    private List<Announcement> announcements;

    private String location;

    private String eventUUID;


    public Event(){

    }

    public Event(String eventName, String eventPoster, String eventDesc, int maxCapacity, Profile organizer, String location, Timestamp eventStartTime) {
        this.eventName = eventName;
        this.eventPoster = eventPoster;
        this.eventDesc = eventDesc;
        this.maxCapacity = maxCapacity;
        this.eventStartTime = eventStartTime;
        this.organizer = organizer;
        this.location = location;

    }

    // Need to add getters/setters


    public String getEventUUID() {
        return eventUUID;
    }

    public void setEventUUID(String eventUUID) {
        this.eventUUID = eventUUID;
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

    public int getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(int checkInCount) {
        this.checkInCount = checkInCount;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public Timestamp getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Timestamp eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public QR getQrCode() {
        return qrCode;
    }

    public void setQrCode(QR qrCode) {
        this.qrCode = qrCode;
    }

    public List<Profile> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Profile> attendees) {
        this.attendees = attendees;
    }

    public List<Profile> getSignedUp() {
        return signedUp;
    }

    public void setSignedUp(List<Profile> signedUp) {
        this.signedUp = signedUp;
    }

    public Profile getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Profile organizer) {
        this.organizer = organizer;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
