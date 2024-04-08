package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.example.seqr.models.Announcement;
import com.example.seqr.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Controller class to handle interactions between the data base and announcment objects
 */
public class AnnouncementController {
    private final FirebaseFirestore db;
    private final CollectionReference announcementCollection;

    /**
     * Constructor class that gets the database and the appropriate collection
     */
    public AnnouncementController() {
        db = Database.getFireStore();
        announcementCollection = db.collection("Announcements");
    }

    /**
     * Constructor class for testing
     * @param db
     */
    public AnnouncementController(FirebaseFirestore db){
        this.db = db;
        this.announcementCollection = db.collection("Announcements");
    }

    /**
]
     * add an announcment to firebase
     *
     * @param announcement announcment type object to add to db
     */
    public void addAnnouncement(Announcement announcement) {
        announcementCollection.document(announcement.getAnnouncementID()).set(announcement).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Debug", "Successfully added announcement");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Debug","Failure to add announcement",e);
            }
        });
    }

    /**
     * Method that removes an announcment from firebase based on its id
     *
     * @param announcementID a string representing the ID of the announcment
     */
    public void removeAnnouncementByID(String announcementID){
        announcementCollection.document(announcementID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Debug","Successfully Deleted");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debug","Cant delete",e);
                    }
                });
    }


    /**
     * finds an event and gets all the announcements associated with it
     *
     * @param eventID the ID associated with a certain event in the database
     * @param onCompleteListener a method to handle what to do with the retrieved announcements
     */
    public void getAnnouncementsByEvent(String eventID, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        announcementCollection
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * gets a certain announcement from the database from its associated ID
     *
     * @param announcementID a string representing the ID of a certain announcement
     * @param onCompleteListener a method to handle the found announcement
     */
    public void getAnnouncementById(String announcementID, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        announcementCollection.document(announcementID).get().addOnCompleteListener(onCompleteListener);
    }
}
