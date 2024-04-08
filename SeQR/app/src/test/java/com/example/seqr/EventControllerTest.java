package com.example.seqr;

import com.example.seqr.controllers.EventController;
import com.example.seqr.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.robolectric.RobolectricTestRunner;
import static org.mockito.ArgumentMatchers.anyString;
import org.robolectric.annotation.Config;

/**
 * EventControllerTest class is responsible for testing the functionalities of the EventController class.
 * It utilizes RobolectricTestRunner to run the tests and Config.NONE for configuration.
 */
@RunWith(RobolectricTestRunner.class) //Run the tests using Robolectric
@Config(manifest = Config.NONE)
public class EventControllerTest {

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private CollectionReference mockEventCollection;

    @Mock
    private DocumentReference mockEventDoc;

    private EventController eventController;

    /**
     * Sets up the necessary mocks and initializes the EventController instance for testing.
     */
    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        when(mockFirestore.collection("Events")).thenReturn(mockEventCollection);

        when(mockEventCollection.document(anyString())).thenReturn(mockEventDoc);

        eventController = new EventController(mockFirestore);
    }

    /**
     * Test case to verify the functionality of adding an event.
     */
    @Test
    public void testAddEvent(){
        Event event = new Event();
        event.setEventID("testID");
        Task<Void> mockTask = Tasks.forResult(null);

        when(mockEventDoc.set(event)).thenReturn(mockTask);

        eventController.addEvent(event);

        verify(mockEventCollection).document("testID");
        verify(mockEventDoc).set(event);
    }

    /**
     * Test case to verify the functionality of removing an event.
     */
    @Test
    public void testRemoveEvent() {
        Event event = new Event();
        event.setEventID("testID");

        when(mockEventDoc.delete()).thenReturn(Tasks.forResult(null));

        eventController.removeEvent(event);

        verify(mockEventCollection).document("testID");
        verify(mockEventDoc).delete();
    }


}
