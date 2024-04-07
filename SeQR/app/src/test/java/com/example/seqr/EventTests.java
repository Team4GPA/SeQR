package com.example.seqr;

import static junit.framework.TestCase.assertEquals;

import com.example.seqr.models.Event;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * A package of test methods for the Event model.
 *
 * @author Kyle Zwarich
 */
public class EventTests {

    /**
     * Makes a mock event class for testing purposes.
     */
    public Event MakeMock(){
        Event mockEvent = new Event();
        assert(mockEvent.getEventName() == null);
        return mockEvent;
    }

    /**
     * Setup a bunch of default event detail strings for testing
     * @return An array of strings for use later
     */
    public String[] DefaultEventDetails(){
        String defaults[] = {
                "Default Name",
                "Default Description",
                "Default Location",
                "Default Start Time"};

        return defaults;
    }

    /**
     * Create a blank event, add its name, and ensure the name was set properly
     */
    @Test
    public void SimpleAddEventTest(){
        String mockData[] = DefaultEventDetails();
        Event blank = MakeMock();

        //add data to the event;
        assert(blank.getEventName() == null);
        blank.setEventName(mockData[0]);
        assertEquals(mockData[0], blank.getEventName());
    }

    /**
     * Create a blank event, add a name, change the name, and ensure it changed properly.
     */
    @Test
    public void ChangeEventDetailsTest(){
        String mockData[] = DefaultEventDetails();
        Event blank = MakeMock();

        //add data to the event;
        assert(blank.getEventName() == null);
        blank.setEventName(mockData[0]);
        assertEquals(mockData[0], blank.getEventName());
        String newName = "New Event Name";
        blank.setEventName(newName);
        assertEquals(newName, blank.getEventName());
    }

    /**
     * Fill every part of the event model with some kind of appropriate data
     * to test the constructor.
     */
    @Test
    public void FillAllEventData(){
        String mockData[] = DefaultEventDetails();
        Event fillMe = new Event();

        assertEquals(mockData[0], fillMe.getEventName());

    }

    /**
     * A fragment to retrieve the defined attributes of an Event model
     * @param klazz
     * @return An array of reflected Fields set in the Event class
     * @param <T>
     *
     * From https://stackoverflow.com/questions/3333974/how-to-loop-over-a-class-attributes-in-java
     */
    public <T> Field[] inspect(Class<Event> klazz){
        Field fields[] = klazz.getDeclaredFields();
        return fields;
    }

}