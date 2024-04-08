package com.example.seqr;

import static junit.framework.TestCase.assertEquals;
import com.example.seqr.models.Profile;
import org.junit.Test;
import java.util.ArrayList;

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

        return new String[]{
                "Default Username",
                "Default Email",
                "Default id",
                "Default ProfilePic"};
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
    public void ChangeDetailsTest(){
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
    public void FillAllData(){
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

    @Test
    public void TestSettersGetters(){
        Profile mockProfile = MakeMockProfile();

        //setters;
        mockProfile.setAdmin(true);
        String mockURI = "MOCK URI:///";
        mockProfile.setProfilePic(mockURI);
        String mockEMAIL = "mock@mock.com";
        mockProfile.setEmail(mockEMAIL);
        String mockID = "mockUUID";
        mockProfile.setId(mockID);
        String mockFCM = "mockFCMtoken";
        mockProfile.setFcmToken(mockFCM);
        mockProfile.setGeoLocation(true);
        String mockWEB = "http://mockweb.com";
        mockProfile.setHomePage(mockWEB);
        String mockPHONE = "(555) 555-5555";
        mockProfile.setPhoneNumber(mockPHONE);
        String mockNAME = "Mock Name";
        mockProfile.setUsername(mockNAME);
        String mockPICURI = "CONTENT:///MOCKURIPATH";
        mockProfile.setProfilePic(mockPICURI);
        ArrayList<String> mockEVENTS = new ArrayList<>();
        mockEVENTS.add("Mock MyEvent 1");
        mockEVENTS.add("Mock MyEvent 2");
        mockProfile.setCreatedEvents(mockEVENTS);
        ArrayList<String> mockNOTIF = new ArrayList<>();
        mockNOTIF.add("Mock NOTIF 1");
        mockNOTIF.add("Mock NOTIF 2");
        mockProfile.setNotifications(mockNOTIF);
        ArrayList<String> mockSIGNUPS = new ArrayList<>();
        mockSIGNUPS.add("Mock MY Signup 1");
        mockProfile.setSignedUpEvents(mockSIGNUPS);

        String rEMAILres = mockProfile.getEmail();
        String rNAMEres = mockProfile.getUsername();
        String rIDres = mockProfile.getId();
        String rFCMres = mockProfile.getFcmToken();
        String rHTTPres = mockProfile.getHomePage();
        String rPICURIres = mockProfile.getProfilePic();
        ArrayList<String> rNOTIFres = (ArrayList<String>) mockProfile.getNotifications();
        ArrayList<String> rCREATEres = (ArrayList<String>) mockProfile.getCreatedEvents();
        ArrayList<String> rMYEVENTres = (ArrayList<String>) mockProfile.getSignedUpEvents();

        assertEquals(mockEMAIL, rEMAILres);
        assertEquals(mockNAME, rNAMEres);
        assertEquals(mockWEB, rHTTPres);
        assertEquals(mockID, rIDres);
        assertEquals(mockSIGNUPS.get(0), rMYEVENTres.get(0));
        assertEquals(mockNOTIF.get(0), rNOTIFres.get(0));
        assertEquals(mockFCM, rFCMres);
        assertEquals(mockPICURI, rPICURIres);
        assertEquals(mockEVENTS.get(0), rCREATEres.get(0));
    }
}
