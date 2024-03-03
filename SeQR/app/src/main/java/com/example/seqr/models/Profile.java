package com.example.seqr.models;
import java.util.List;

public class Profile {
    private String username;
    private String email;
    private String phoneNumber;
    private String homePage;
    private boolean geoLocation;
    private boolean isAdmin;
    private String id;
    private String profilePic;
    private List<Event> createdEvents; //list of Events created by this profile
    private List<Event> signedUpEvents; //list of signedUpEvents by this profile



    //constructor for a profile
    public Profile(String username, String ID){
        this.username = username;
        this.id = ID;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public boolean isGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(boolean geoLocation) {
        this.geoLocation = geoLocation;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(List<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public List<Event> getSignedUpEvents() {
        return signedUpEvents;
    }

    public void setSignedUpEvents(List<Event> signedUpEvents) {
        this.signedUpEvents = signedUpEvents;
    }
}
