package com.example.seqr;

import static junit.framework.TestCase.assertEquals;
import com.example.seqr.models.Event;
import com.google.firebase.Timestamp;
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
     * Create a blank event, add its name, and ensure the name was set properly
     */
    @Test
    public void SimpleAddEventTest(){
        Event blank = MakeMock();

        //add data to the event;
        assert(blank.getEventName() == null);
        blank.setEventName("Mock Event Title");
        assertEquals("Mock Event Title", blank.getEventName());
    }

    /**
     * Create a blank event, add a name, change the name, and ensure it changed properly.
     */
    @Test
    public void ChangeEventDetailsTest(){
        Event blank = MakeMock();
        String name = "Mock Name";

        //add data to the event;
        assert(blank.getEventName() == null);
        blank.setEventName(name);
        assertEquals(name, blank.getEventName());
        String newName = "Update Mock Name";
        blank.setEventName(newName);
        assertEquals(newName, blank.getEventName());
    }

    /**
     * Fill every part of the event model with some kind of appropriate data
     * to test the constructor.
     */
    @Test
    public void FillAllEventData(){
        Event fillMe = new Event("Mock Name", "Mock ID", "Mock Description",
                4, "Mock Organizer", "Mock Location", Timestamp.now(),
                "MockQRString-Promo", "MockQRString-CheckIn",
        "Mock Organizer UUID", Timestamp.now(), 45.00, 49.00);

        String grabQR = fillMe.getCheckInQR();
        assertEquals("MockQRString-CheckIn", grabQR);

        String grabOrgUUID = fillMe.getOrganizerUUID();
        assertEquals("Mock Organizer UUID", grabOrgUUID);

        Timestamp grabCreated = fillMe.getCreatedTime();
        TestIneqFloat isGTE = new TestIneqFloat();
        isGTE.lhs = (double) Timestamp.now().getSeconds();
        isGTE.rhs = (double) grabCreated.getSeconds();
        isGTE.testGTE();
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
        return klazz.getDeclaredFields();
    }

}