package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.example.seqr.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Controller class for managing Profile data in database.
 */
public class ProfileController {
    private final FirebaseFirestore db;
    private final CollectionReference profileCollection;

    /**
     * Constructs a ProfileController and initializes Firestore database reference.
     */
    public ProfileController(){

        db = Database.getFireStore();
        profileCollection = db.collection("Profiles");

    }

    /**
     * constructor for testing
     * @param db this will be a mock DB for testing
     */

    public ProfileController(FirebaseFirestore db){
        this.db = db;
        profileCollection = db.collection("Profiles");
    }

    /**
     * Adds a profile to Firestore.
     *
     * @param profile The Profile object to add.
     */
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

    /**
     * Retrieves profile username by device ID (uniquely for this device).
     *
     * @param deviceId The device ID associated with the profile.
     * @param onCompleteListener Listener to be invoked when the data retrieval is complete.
     */
    public void getProfileUsernameByDeviceId(String deviceId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        profileCollection.document(deviceId).get().addOnCompleteListener(onCompleteListener);
    }

    /**
     * Retrieves all profiles from database.
     *
     * @param onCompleteListener Listener to be invoked when the data retrieval is complete.
     */
    public void getAllProfiles(OnCompleteListener<QuerySnapshot> onCompleteListener){
        profileCollection.get().addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","issue with getting all profiles from firebase");
                    }
                });
    }

    /**
     * Updates geolocation setting for a profile (device).
     *
     * @param uuid The UUID of the profile (device).
     * @param enableGeoLocation Boolean value indicating whether geolocation tracking is enabled.
     */
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

    /**
     * Updates profile information in database.
     *
     * @param uuid The UUID of the profile (device).
     * @param username The updated username.
     * @param phonenumber The updated phone number.
     * @param email The updated email.
     * @param homepage The updated homepage.
     */
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

    /**
     * Updates profile picture URL in database.
     *
     * @param uuid The UUID of the profile (device).
     * @param ImageURL The updated profile picture URL.
     * @param onSuccessListener Listener to be invoked when the operation is successful.
     * @param onFailureListener Listener to be invoked when the operation fails.
     */
    public void updatePFP(String uuid, String ImageURL, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        profileCollection.document(uuid)
                .update("profilePic", ImageURL)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }


    /**
     * Retrieves the document reference for a profile by device ID.
     *
     * @param deviceId The device ID associated with the profile.
     * @return The DocumentReference object for the profile.
     */
    public DocumentReference getProfileDocument(String deviceId) {
        return profileCollection.document(deviceId);
    }

    /**
     * Method to delete a user's profile in firebase
     *
     * @param userID string representing the ID of the user
     */
    public void deleteProfile(String userID){
        profileCollection.document(userID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        deleteProfilePicture(userID);
                        removeUserFromEventSignups(userID);
                        Log.d("DEBUG", "Profile deleted successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","Failed to delete profile",e);
                    }
                });
    }

    /**
     * Deletes a user's profile picture in firebase
     *
     * @param userID string representing the ID of the user
     */
    public void deleteProfilePicture(String userID){
        StorageReference profilePicReference = Database.getStorage().getReference().child("ProfilePictures/" + userID + ".jpg");
        profilePicReference.delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG", "Failed to delete profile picture", e);
            }
        });
    }

    /**
     * removes a user from all events they are signed up for in firebase
     *
     * @param userID string representing the ID of the user
     */
    private void removeUserFromEventSignups(String userID){
        db.collection("Events").whereArrayContains("signups", userID).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot eventSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String eventID = eventSnapshot.getId();
                            db.collection("Events").document(eventID).collection("signups")
                                    .document(userID).delete()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("DEBUG", "Failed to remove user from event");
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "Failed to retrieve events user is signed up for",e);
                    }
                });
    }
    /**
     * This function will update the users signedUpEvents field in firebase with the new events added to the signedUpEvents
     * @param uuid The UUID of the profile
     * @param signedUpEvents the users signedUpEvents
     */
    public void signedUpEventsUpdater(String uuid, List<String> signedUpEvents){
        profileCollection.document(uuid)
                .update("signedUpEvents", signedUpEvents)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DEBUG","Successfully updated the users signedUpEvents");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","Error with updating the users signedUpEvents",e);
                    }
                });
    }

    /**
     * Updates a profile's list of notifications
     *
     * @param uuid string representing the ID of the user
     * @param notifications list of strings that represent notifications sent to the user
     */
    public void notificationsUpdater(String uuid, List<String> notifications) {
        profileCollection.document(uuid)
                .update("notifications", notifications)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DEBUG","Successfully updated the users notifications");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","Error with updating the users notifications",e);
                    }
                });
    }

    /**
     * Updates the FCM token for a user
     * @param uuid string representing the ID of the user
     */
    public void updateFCMToken(String uuid) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String fcmToken = task.getResult();
                    profileCollection.document(uuid)
                            .update("fcmToken", fcmToken)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("DEBUG","Successfully updated the fcm token");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("DEBUG","Error with updating the fcm token",e);
                                }
                            });
                }
            }
        });
    }

    /**
     * Gets a profile By a given ID
     * @param uuid string representing the ID of the user
     * @param onCompleteListener method to handle the profile
     */
    public void getProfileByUUID(String uuid, OnCompleteListener<DocumentSnapshot> onCompleteListener){
        profileCollection.document(uuid).get().addOnCompleteListener(onCompleteListener);
    }

    /**
     * Get all notification's for a user
     *
     * @param uuid string representing the ID of the user
     * @param onCompleteListener method to handle the notifications
     */
    public void getNotificationsByUUID(String uuid, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        profileCollection.document(uuid).get().addOnCompleteListener(onCompleteListener);
    }
}
