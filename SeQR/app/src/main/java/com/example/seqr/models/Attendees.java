package com.example.seqr.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class Attendees {
    private String userName;
    private String profile_picture;
    private List<Timestamp> checkIns;

    public Attendees(String userName, String profile_picture, Timestamp time) {
        this.userName = userName;
        this.profile_picture = profile_picture;
        this.checkIns.add(time);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public List<Timestamp> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(List<Timestamp> checkIns) {
        this.checkIns = checkIns;
    }
}
