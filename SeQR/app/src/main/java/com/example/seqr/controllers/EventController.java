package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.example.seqr.models.Event;
import com.example.seqr.models.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

/**
 * Controller class for managing Event data in Firestore database.
 */
public class EventController {
    private FirebaseFirestore db;
    private CollectionReference eventCollection;
    //insert any new database fields here for checkEventValid
    private final String[] databaseFields = {
            "checkInQR",
            "eventDesc",
            "eventID",
            "eventName",
            "eventStartTime",
            "location",
            "maxCapacity",
            "organizer",
            "organizerUUID",
            "promotionQR",
    };

    /**
     * Constructs an EventController and initializes Firestore database reference.
     */
    public EventController(){
        db = Database.getFireStore();
        eventCollection = db.collection("Events");
    }


    /**
     * Adds an event to database.
     *
     * @param event The Event object to add.
     */
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

    /**
     * Removes an event from database.
     *
     * @param event The Event object to remove.
     */
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

    /**
     * Retrieves all events from database.
     *
     * @param onCompleteListener Listener to be invoked when the data retrieval is complete.
     */
    public void getAllEvents(OnCompleteListener<QuerySnapshot> onCompleteListener){
        eventCollection.get().addOnCompleteListener(onCompleteListener);
    }

    /**
     * Retrieves events from database by organizer UUID (only for the specific organizer).
     *
     * @param organizerUUID The UUID of the organizer.
     * @param onCompleteListener Listener to be invoked when the data retrieval is complete.
     */
    //gets the events from the specific organizer/ basically passes in the profile/device ID.
    public void getEventsByOrganizer(String organizerUUID, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        eventCollection
                .whereEqualTo("organizerUUID", organizerUUID)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * Retrieves the event by the given EventID
     * @param eventId eventID of the event
     * @param onCompleteListener Listener ot be invoked when data retrieval is complete
     */

    public void getEventById(String eventId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        eventCollection.document(eventId).get().addOnCompleteListener(onCompleteListener);
    }


    /**
     * Signs the user up for an event
     * @param eventId the eventID of the event
     * @param signUp the signUp object that contains the id and username of someone who signed up
     */
    public void signUserUpForEvent(String eventId, SignUp signUp) {
        db.collection("Events").document(eventId).collection("signups")
                .document(signUp.getUserId()).set(signUp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d("Debug","Successfully signedUp");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debug","failedToSignup");
                    }
                });
    }

    public void getEventSignUps(String eventID, OnCompleteListener<QuerySnapshot> onCompleteListener){
        eventCollection.document(eventID).collection("signups")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getEventCheckIns(String eventID, OnCompleteListener<QuerySnapshot> onCompleteListener){
        eventCollection.document(eventID).collection("checkIns")
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","Issue with getting the checkIns document");
                    }
                });
    }

    public void checkInUser (String eventID, String userID, String username, OnSuccessListener<Void> onSuccessListener,OnFailureListener onFailureListener){
        DocumentReference checkInDocRef = eventCollection.document(eventID)
                .collection("checkIns")
                .document(userID);
        Map<String, Object> checkInData = new HashMap<>();
        checkInData.put("username",username);
        Timestamp time = Timestamp.now();
        checkInData.put("checkInTimes", FieldValue.arrayUnion(time));
        checkInDocRef.set(checkInData, SetOptions.merge())
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void getUserCheckIns(String eventID, String userId, OnCompleteListener<DocumentSnapshot> onCompleteListener){
        eventCollection.document(eventID).collection("checkIns")
                .document(userId)
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","issue with getting the user checkIns");
                    }
                });
    }


    /**
     * Checks an event document for validity of all fields.
     * A single event fields are defined in the databaseFields array in this class; see EventController's
     * private attributes to modify this array.
     * <ul>
     * <li>checkInQR       string      a QR code PNG converted into base64 to enable storage as a string
     * <li>eventDesc       string      the description of the event
     * <li>eventID         string      a 128-bit unique identifier randomly generated upon event creation
     * <li>eventName       string      the event's name as entered by the user
     * <li>eventStartTime  string      an arbitrary string allowing for flexible time entry
     * <li>location        string      a text field describing the event location
     * <li>maxCapacity     number      value for the maximum capacity of an event
     * <li>organizer       string      the user's entered name during first-load of the SeQR app
     * <li>organizerUUID   string      generated UUID from the device, or created during event creation
     * <li>promotionQR     string      see checkInQR above
     * </ul>
     * @param eventID The eventID to check
     * @author Kyle Zwarich
     */
    public void checkEventValid(String eventID){
        final DocumentSnapshot[] dbEvent = new DocumentSnapshot[1];
        eventCollection.document(eventID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dbEvent[0] = task.getResult();
                for (String field : databaseFields) {
                    if (!dbEvent[0].contains(field)) {
                        Log.d("EVENT CONTROLLER", "Event with ID " + eventID + " is missing field: " + field);
                    } else if (dbEvent[0].contains(field) && (dbEvent[0].get(field) == null || dbEvent[0].get(field) == "null")) {
                        Log.d("EVENT CONTROLLER", "Event with ID" + eventID + " has a null entry for field: " + field);
                    }
                }
            }
        });
    }




}
