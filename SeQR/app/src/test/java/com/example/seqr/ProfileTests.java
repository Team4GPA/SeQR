package com.example.seqr;

import static junit.framework.TestCase.assertEquals;

import com.example.seqr.models.Event;
import com.example.seqr.models.Profile;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * A package of test methods for Profile Model
 *
 * @author Namra Kanani
 */
public class ProfileTests {

    /**
     * make mock profile class for testing purposes
     */
    public Profile MakeMockProfile(){
        Profile mProfile = new Profile();
        assert(mProfile.getUsername() == null);
        return mProfile;
    }

    /**
     * Setup a bunch of default profile detail strings for testing
     * @return An array of strings for use later
     */
    public String[] DefaultProfileDetails(){
        String[] defaults = {
                "Default Username",
                "Default Email",
                "Default id",
                "Default ProfilePic"};

        return defaults;
    }

    /**
     * Create a blank profile, add its username, and ensure the username was set properly
     */
    @Test
    public void SimpleAddProfileTest(){
        String[] mockData = DefaultProfileDetails();
        Profile blank = MakeMockProfile();

        //add data to the profile;
        assert(blank.getUsername() == null);
        blank.setUsername(mockData[0]);
        assertEquals(mockData[0], blank.getUsername());
    }

    /**
     * Create a blank profile, add a username, change the username, and ensure it changed properly.
     */
    @Test
    public void ChangeEventDetailsTest(){
        String[] mockData = DefaultProfileDetails();
        Profile blank = MakeMockProfile();

        //add data to the event;
        assert(blank.getUsername() == null);
        blank.setUsername(mockData[0]);
        assertEquals(mockData[0], blank.getUsername());
        String newName = "New Profile username";
        blank.setUsername(newName);
        assertEquals(newName, blank.getUsername());
    }

    /**
     * Fill every part of the profile model with some kind of appropriate data
     * to test the constructor.
     */
    @Test
    public void FillAllEventData(){
        String[] mockData = DefaultProfileDetails();
        Profile fillMe = new Profile(mockData[0],
                                    mockData[1],
                                    "911",
                                    "testing homepage",
                                    mockData[2],
                                    mockData[3]);

        assertEquals(mockData[0], fillMe.getUsername());
        assertEquals(mockData[1], fillMe.getEmail());
        assertEquals("911", fillMe.getPhoneNumber());
        assertEquals("testing homepage", fillMe.getHomePage());
        assertEquals(mockData[2], fillMe.getId());
        assertEquals(mockData[3], fillMe.getProfilePic());
    }
}
