package com.example.seqr.controllers;

import com.example.seqr.database.Database;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnnouncementController {
    private FirebaseFirestore db;
    private CollectionReference eventCollection;

    public AnnouncementController() {
        db = Database.getFireStore();
        eventCollection = db.collection("Announcements");
    }


}
