package com.example.seqr;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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


@RunWith(RobolectricTestRunner.class) //Run the tests using Robolectric
@Config(manifest = Config.NONE) // dont need the manifest files for this test
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

    @Test
    public void testAddAnnouncement(){
        Announcement announcement = new Announcement(); //a new announcement object
        announcement.setAnnouncementID("Testing announcementID"); //set its id
        Task<Void> mockTask = Tasks.forResult(null); //makes a mock task object that has no result this is for methods that just have no return value just success or failure

        //when we call mockAnnouncementDoc.set(announcement) it should return a task
        when(mockAnnouncementDoc.set(announcement)).thenReturn(mockTask);
        
        announcementController.addAnnouncement(announcement);

        verify(mockAnnouncementCollection).document("Testing announcementID");
        verify(mockAnnouncementDoc).set(announcement);
    }

    @Test
    public void testRemoveAnnouncementByID(){
        Task<Void> mockTask = Tasks.forResult(null);
        when(mockAnnouncementDoc.delete()).thenReturn(mockTask);

        announcementController.removeAnnouncementByID("Testing announcementID");
        verify(mockAnnouncementCollection).document("Testing announcementID");
        verify(mockAnnouncementDoc).delete();

    }
}
