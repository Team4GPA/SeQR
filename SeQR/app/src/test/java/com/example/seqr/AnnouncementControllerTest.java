package com.example.seqr;

import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.models.Announcement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Test class for AnnouncementController which basically checks that AnnouncementController methods work with firebase as wanted
 */


@RunWith(RobolectricTestRunner.class) //Run the tests using Robolectric
@Config(manifest = Config.NONE) // don't need the manifest files for this test
public class AnnouncementControllerTest {
    //creates a mock object for firestore
    @Mock
    private FirebaseFirestore mockFirestore;
    //creates a mock collection reference
    @Mock
    private CollectionReference mockAnnouncementCollection;
    //makes a mock document reference
    @Mock
    private DocumentReference mockAnnouncementDoc;

    //announcement controller instance which we are testing
    private AnnouncementController announcementController;


    //this should be run before every test

    /**
     * Basically the before tests setUp, this initializes the mock Objects
     */

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this); // initializes mockFireStore, mockAnnouncementCollection, mockAnnouncementDoc
        //This is basically mocking getting a collection Announcements from firestore
        //when we call mockFirestore.collection("Announcements") we want mockAnnouncementCollection returned
        when(mockFirestore.collection("Announcements")).thenReturn(mockAnnouncementCollection);

        //mocks getting a document from firestore, basiclly saying when we call mockAnnouncementCollection.document(anyString())
        //return a mockDoc
        when(mockAnnouncementCollection.document(anyString())).thenReturn(mockAnnouncementDoc);
        //create an announcementController with the mocked firestore
        announcementController = new AnnouncementController(mockFirestore);
    }

    /**
     * This tests the addAnnouncement method in announcement controller
     */
    @Test
    public void testAddAnnouncement(){
        Announcement announcement = new Announcement(); //a new announcement object
        announcement.setAnnouncementID("Testing announcementID"); //set its id
        Task<Void> mockTask = Tasks.forResult(null); //makes a mock task object that has no result this is for methods that just have no return value just success or failure

        //when we call mockAnnouncementDoc.set(announcement) it should return a task
        when(mockAnnouncementDoc.set(announcement)).thenReturn(mockTask);

        //call addAnnouncement
        announcementController.addAnnouncement(announcement);

        //this verifies that we are trying to get the right document in firebase
        verify(mockAnnouncementCollection).document("Testing announcementID");
        //basically asserting that we tried to write to mockAnnouncementDoc the announcement object
        verify(mockAnnouncementDoc).set(announcement);
    }

    /**
     * This tests the removeAnnouncementById method in announcement controller
     */
    @Test
    public void testRemoveAnnouncementByID(){
        Task<Void> mockTask = Tasks.forResult(null); //makes a mock task object that has no result this is for methods that just have no return value just success or failure
        //when we call delete return the mockTask
        when(mockAnnouncementDoc.delete()).thenReturn(mockTask);

        //call the removeAnnouncementByID
        announcementController.removeAnnouncementByID("Testing announcementID");
        //verify we are calling it on the right document
        verify(mockAnnouncementCollection).document("Testing announcementID");
        // verify the delete method was called on the right doc
        verify(mockAnnouncementDoc).delete();

    }
}
