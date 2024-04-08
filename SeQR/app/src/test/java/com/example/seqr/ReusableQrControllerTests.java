package com.example.seqr;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.example.seqr.controllers.ReusableQrController;

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
 * This is a test class for reusableQR controller which is basically checking if the methods in the class work with firebase as wanted
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ReusableQrControllerTests {

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private CollectionReference mockReusableQrCollection;

    @Mock
    private DocumentReference mockQrPairDoc;

    private ReusableQrController reusableQrController;

    /**
     * Initializer for before the tests, initializes the mock objects
     */

    @Before
    public void initializer() {
        MockitoAnnotations.openMocks(this);//initialize mock objects
        //when we call mockFirestore.collection("ReusableQR") we want mockReusableQrCollection returned
        when(mockFirestore.collection("ReusableQR")).thenReturn(mockReusableQrCollection);
        //basically mocks getting a firebase doc, when we call .document on the mockResuableQrCollection we want a mockQrPairDoc returned
        when(mockReusableQrCollection.document(anyString())).thenReturn(mockQrPairDoc);
        reusableQrController = new ReusableQrController(mockFirestore);
    }

    /**
     * Tests the addQRpair method in the  reusable qr controller
     */

    @Test
    public void testAddQRpair(){
        //makes a mock task object that has no result this is for methods that just have no return value just success or failure
        Task<Void> mockTask = Tasks.forResult(null);
        //when we call mockQrPairDoc.set(any()) return the mockTask
        when(mockQrPairDoc.set(any())).thenReturn(mockTask);

        //use the addQRpair method
        reusableQrController.addQRpair("TestCheckInQR","TestPromotionQR","TestPrevEventName","TestEventID");

        //this verifies that we are trying to get the right document in firebase
        verify(mockReusableQrCollection).document("TestEventID");
        //basically asserting that we tried to write to mockQrPairDoc
        verify(mockQrPairDoc).set(any());
    }

    /**
     * Tests the deleteQRpair method in the reusable qr controller
     */

    @Test
    public void testDeleteQRpair(){
        //makes a mock task object that has no result this is for methods that just have no return value just success or failure
        Task<Void> mockTask = Tasks.forResult(null);
        // when we call mockQrPairDoc.delete() return the mock task
        when(mockQrPairDoc.delete()).thenReturn(mockTask);

        //call deleteQRpair
        reusableQrController.deleteQRPair("TestEventID");
        //verify we are trying to access the correct doc in the mockreusableQRcollection
        verify(mockReusableQrCollection).document("TestEventID");
        //verify we are attempting the delete method on that doc
        verify(mockQrPairDoc).delete();
    }

}
