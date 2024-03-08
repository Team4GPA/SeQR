package com.example.seqr.models;
import java.util.List;

/**
 * A model class that represents a user profile with details such as username, email, phone number, etc.
 */
public class Profile {
    private String username;
    private String email;
    private String phoneNumber;
    private String homePage;
    private boolean geoLocation;
    private boolean isAdmin;
    private String id;
    private String profilePic;
    private List<String> createdEvents; //list of Events created by this profile
    private List<String> signedUpEvents; //list of signedUpEvents by this profile
    private List<Notification> notifications;

    /**
     * Default constructor for the Profile class.
     */
    public Profile(){

    }

    /**
     * Constructor for creating a profile with username and ID.
     *
     * @param username The username of the profile.
     * @param ID       The unique identifier (device ID) of the profile.
     */
    //constructor for a profile
    public Profile(String username, String ID){
        this.username = username;
        this.id = ID;
    }

    /**
     * Sets the unique identifier of the profile.
     *
     * @param id The unique identifier (device ID) to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the list of notifications associated with the profile.
     *
     * @return The list of notifications.
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Sets the list of notifications associated with the profile.
     *
     * @param notifications The list of notifications to set.
     */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * Retrieves the unique identifier of the profile.
     *
     * @return The unique identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the username of the profile.
     *
     * @return The username of the profile.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the profile.
     *
     * @param username The username of the profile.
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Retrieves the email associated with the profile.
     *
     * @return The email associated with the profile.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email associated with the profile.
     *
     * @param email The email associated with the profile.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Retrieves the phone number associated with the profile.
     *
     * @return The phone number associated with the profile.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number associated with the profile.
     *
     * @param phoneNumber The phone number associated with the profile.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves the homepage associated with the profile.
     *
     * @return The homepage associated with the profile.
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * Sets the homepage associated with the profile.
     *
     * @param homePage The homepage associated with the profile.
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }


    /**
     * Checks if geolocation is enabled for the profile.
     *
     * @return True if geolocation is enabled, false otherwise.
     */
    public boolean isGeoLocation() {
        return geoLocation;
    }

    /**
     * Sets whether geolocation is enabled for the profile.
     *
     * @param geoLocation True to enable geolocation, false to disable.
     */
    public void setGeoLocation(boolean geoLocation) {
        this.geoLocation = geoLocation;
    }


    /**
     * Checks if the profile is an admin.
     *
     * @return True if the profile is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets whether the profile is an admin.
     *
     * @param admin True to set the profile as admin, false otherwise.
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Retrieves the profile picture URL.
     *
     * @return The profile picture URL.
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Sets the profile picture URL.
     *
     * @param profilePic The profile picture URL.
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

<<<<<<< HEAD
    /**
     * Retrieves the list of events created by this profile.
     *
     * @return The list of events created by this profile.
     */
    public List<Event> getCreatedEvents() {
        return createdEvents;
    }

    /**
     * Sets the list of events created by this profile.
     *
     * @param createdEvents The list of events created by this profile.
     */
    public void setCreatedEvents(List<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    /**
     * Retrieves the list of events signed up by this profile.
     *
     * @return The list of events signed up by this profile.
     */
    public List<Event> getSignedUpEvents() {
        return signedUpEvents;
    }

    /**
     * Sets the list of events signed up by this profile.
     *
     * @param signedUpEvents The list of events signed up by this profile.
     */
    public void setSignedUpEvents(List<Event> signedUpEvents) {
=======
    public List<String> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(List<String> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public List<String> getSignedUpEvents() {
        return signedUpEvents;
    }

    public void setSignedUpEvents(List<String> signedUpEvents) {
>>>>>>> aa518e3d45d3bab48b9272daf41ee734d31cf815
        this.signedUpEvents = signedUpEvents;
    }
}
