package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.example.seqr.models.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
public class EventController {
    private FirebaseFirestore db;
    private CollectionReference eventCollection;

    public EventController(){
        db = Database.getFireStore();
        eventCollection = db.collection("Events");
    }

    public void addEvent(Event event){
        eventCollection.document(event.getEventID()).set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Debug", "Successfully added event");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Debug","Failure to add event",e);
            }
        });
    }

    public void removeEvent(Event event){
        eventCollection.document(event.getEventID()).delete()
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
}
