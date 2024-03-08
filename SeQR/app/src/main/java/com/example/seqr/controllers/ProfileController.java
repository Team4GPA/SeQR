package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.example.seqr.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    //Takes in the new values and updates them
    public void updateProfile(String uuid, String username, String homePage, Integer phoneNumber, String email){
        profileCollection.document(uuid)
                .update("username",username,"homePage",homePage,"phoneNumber",phoneNumber,"email",email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DEBUG","Successfully updated profile");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "Error updating profile",e);
                    }
                });
    }

    public void getProfileUsernameByDeviceId(String deviceId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        profileCollection.document(deviceId).get().addOnCompleteListener(onCompleteListener);
    }





}
