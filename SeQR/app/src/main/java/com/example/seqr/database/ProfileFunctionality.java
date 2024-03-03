package com.example.seqr.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.example.seqr.models.Profile;
public class ProfileFunctionality {

    private FirebaseFirestore db;
    private CollectionReference profileCollection;

    public ProfileFunctionality(){
        db = Database.getFireStore();
        profileCollection = db.collection("Profiles");

    }

    public void createProfile(Profile profile){
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
