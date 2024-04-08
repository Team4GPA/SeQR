package com.example.seqr;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import com.example.seqr.models.Announcement;

import com.google.firebase.Timestamp;

import org.junit.Test;



public class AnnouncementTests {

    @Test
    public void testSettersGetters(){
        Announcement mockAnnouncement = new Announcement();
        mockAnnouncement.setAnnouncementID("Test ID");
        mockAnnouncement.setDescription("Test Description");
        Timestamp mockTime = Timestamp.now();
        mockAnnouncement.setTime(mockTime);
        mockAnnouncement.setEventID("Test Event ID");
        mockAnnouncement.setTitle("Test Title");
        mockAnnouncement.setOrganizer("Test Organizer");
        assertEquals("Test ID",mockAnnouncement.getAnnouncementID());
        assertEquals("Test Description",mockAnnouncement.getDescription());
        assertEquals("Test Event ID",mockAnnouncement.getEventID());
        assertEquals("Test Title",mockAnnouncement.getTitle());
        assertEquals(mockTime,mockAnnouncement.getTime());
        assertEquals("Test Organizer",mockAnnouncement.getOrganizer());

    }

    @Test
    public void testConstructor(){
        Timestamp mockTime = Timestamp.now();
        Announcement mockAnnouncement = new Announcement("Test Title", "Test Description",mockTime,"Test Event ID","Test ID","Test Organizer");
        assertEquals("Test ID",mockAnnouncement.getAnnouncementID());
        assertEquals("Test Description",mockAnnouncement.getDescription());
        assertEquals("Test Event ID",mockAnnouncement.getEventID());
        assertEquals("Test Title",mockAnnouncement.getTitle());
        assertEquals(mockTime,mockAnnouncement.getTime());
        assertEquals("Test Organizer",mockAnnouncement.getOrganizer());
    }

    @Test
    public void testEmptyConstructor(){
        Announcement mockAnnouncement = new Announcement();
        assertNull(mockAnnouncement.getAnnouncementID());
        assertNull(mockAnnouncement.getDescription());
        assertNull(mockAnnouncement.getEventID());
        assertNull(mockAnnouncement.getTitle());
        assertNull(mockAnnouncement.getTime());
        assertNull(mockAnnouncement.getOrganizer());
    }
}
