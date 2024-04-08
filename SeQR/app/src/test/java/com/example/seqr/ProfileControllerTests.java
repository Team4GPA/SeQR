package com.example.seqr;


import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.example.seqr.controllers.ProfileController;


import com.example.seqr.models.Profile;
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
 * This is a test class for ProfileController which basically is checking if the methods are interacting with firebase as wanted
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ProfileControllerTests {

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private CollectionReference mockProfileCollection;

    @Mock
    private  DocumentReference mockProfileDoc;

    private ProfileController profileController;

    /**
     * This is a n intializer before every test, basically inits the mock objects
     */

    @Before
    public void initializer(){
        MockitoAnnotations.openMocks(this); //init mock objects
        // return mockProfileCollection when call mockFirestore.collection("Profiles")
        when(mockFirestore.collection("Profiles")).thenReturn(mockProfileCollection);
        // when call mockProfileCollection.document(anyString()) return mockProfileDoc
        when(mockProfileCollection.document(anyString())).thenReturn(mockProfileDoc);
        profileController = new ProfileController(mockFirestore);
    }

    /**
     * this tests the addProfile method in the profile controller
     */

    @Test
    public void testAddProfile(){
        //make testProfile object
        Profile testProfile = new Profile("TestUserName","TestUserID",null);
        //makes a mock task object that has no result this is for methods that just have no return value just success or failure
        Task<Void> mockTask = Tasks.forResult(null);

        //when call mockProfileDoc.set(testProfile) return the mock task
        when(mockProfileDoc.set(testProfile)).thenReturn(mockTask);

        // call the add profile
        profileController.addProfile(testProfile);

        //verify we are accessing the correct doc
        verify(mockProfileCollection).document(testProfile.getId());
        //verify we are trying to write to that correct doc
        verify(mockProfileDoc).set(testProfile);
    }

    /**
     * This tests the deleteProfile method in the profile controller
     */

    @Test
    public void testDeleteProfile(){
        //makes a mock task object that has no result this is for methods that just have no return value just success or failure
        Task<Void> mockTask = Tasks.forResult(null);
        when(mockProfileDoc.delete()).thenReturn(mockTask);

        //call deleteProfile method
        profileController.deleteProfile("TestID");

        //verify we are accessing the right document
        verify(mockProfileCollection).document("TestID");

        //verify that we are attempting delete method on that document
        verify(mockProfileDoc).delete();



    }

}
