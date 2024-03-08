package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.example.seqr.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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


    public void getProfileUsernameByDeviceId(String deviceId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        profileCollection.document(deviceId).get().addOnCompleteListener(onCompleteListener);
    }

    public void getAllProfiles(OnCompleteListener<QuerySnapshot> onCompleteListener){
        profileCollection.get().addOnCompleteListener(onCompleteListener);
    }





    public void updateGeoLocation(String uuid, boolean enableGeoLocation) {
        profileCollection.document(uuid)
                .update("geoLocation", enableGeoLocation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DEBUG", "Successfully updated geolocation setting");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "Error updating geolocation setting", e);
                    }
                });
    }


    public void updateProfile(String uuid, String username, String phonenumber, String email, String homepage) {
        profileCollection.document(uuid)
                .update("username", username,"homePage",homepage,"phoneNumber",phonenumber,"email",email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DEBUG", "Successfully updated geolocation setting");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "Error updating geolocation setting", e);
                    }
                });
    }

    public void updatePFP(String uuid, String ImageURL, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        profileCollection.document(uuid)
                .update("profilePic", ImageURL)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public DocumentReference getProfileDocument(String deviceId) {
        return profileCollection.document(deviceId);
    }
}
