package com.example.seqr.models;

import com.google.firebase.Timestamp;

import java.util.List;


/**
 * A model class representing attendees (who checked in) of an event.
 */
public class Attendees {
    private String userName;
    private String profile_picture;
    private List<Timestamp> checkIns;

    /**
     * Constructs an Attendees object with the given username, profile picture, and check-in time.
     *
     * @param userName        The username of the attendee.
     * @param profile_picture The URL of the attendee's profile picture.
     * @param time            The timestamp indicating the check-in time.
     */
    public Attendees(String userName, String profile_picture, Timestamp time) {
        this.userName = userName;
        this.profile_picture = profile_picture;
        this.checkIns.add(time);
    }

    /**
     * Retrieves the username of the attendee.
     *
     * @return The username of the attendee.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the attendee.
     *
     * @param userName The username of the attendee.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Retrieves the profile picture URL of the attendee.
     *
     * @return The URL of the attendee's profile picture.
     */
    public String getProfile_picture() {
        return profile_picture;
    }

    /**
     * Sets the profile picture URL of the attendee.
     *
     * @param profile_picture The URL of the attendee's profile picture.
     */
    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    /**
     * Retrieves the list of check-in times of the attendee.
     *
     * @return The list of check-in times.
     */
    public List<Timestamp> getCheckIns() {
        return checkIns;
    }

    /**
     * Sets the list of check-in times of the attendee.
     *
     * @param checkIns The list of check-in times.
     */
    public void setCheckIns(List<Timestamp> checkIns) {
        this.checkIns = checkIns;
    }
}
