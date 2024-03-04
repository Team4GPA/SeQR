package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.example.seqr.models.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileController {
    private FirebaseFirestore db;
    private CollectionReference profileCollection;


    public ProfileController(){

        db = Database.getFireStore();
        profileCollection = db.collection("Profiles");

    }
    // Adds the profile into the firebase Profile collection, the documentid will be the profile ID so it can be easily queried later
    public void addProfile(Profile profile){
        profileCollection.document(profile.getId()).set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("DEBUG","Added Profile");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG","Error adding profile",e);
            }
        });


    }

}
