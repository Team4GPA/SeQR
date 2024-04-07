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

public class AnnouncementController {
    private FirebaseFirestore db;
    private CollectionReference announcementCollection;

    public AnnouncementController() {
        db = Database.getFireStore();
        announcementCollection = db.collection("Announcements");
    }

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

    public void removeAnnouncement(Announcement announcement){
        announcementCollection.document(announcement.getAnnouncementID()).delete()
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

    public void getAnnouncementsByEvent(String eventID, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        announcementCollection
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getAnnouncementById(String announcementID, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        announcementCollection.document(announcementID).get().addOnCompleteListener(onCompleteListener);
    }
}
