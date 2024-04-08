package com.example.seqr.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A controller class for managing reusable QR pairs in Firestore.
 */
public class ReusableQrController {
    private final FirebaseFirestore db;
    private final CollectionReference ReusableQrCollection;



    /**
     * Constructor initializing Firestore database and collection references.
     */
    public ReusableQrController(){
        db = Database.getFireStore();
        ReusableQrCollection = db.collection("ReusableQR");
    }

    /**
     * Add a new QR pair to Firestore.
     * @param checkInQR The QR code for check-in
     * @param promotionQR The QR code for promotion
     * @param previousEventName The name of the previous event
     * @param eventID The ID of the event associated with the QR pair
     */
    public void addQRpair(String checkInQR, String promotionQR, String previousEventName, String eventID){
        //Reference to the document ID
        DocumentReference qrPairDoc = ReusableQrCollection.document(eventID);
        //Create a hashmap to add to the document
        Map<String, Object> qrCodePair = new HashMap<>();
        qrCodePair.put("checkInQR", checkInQR);
        qrCodePair.put("promotionQR",promotionQR);
        qrCodePair.put("previousEventName",previousEventName);
        //add to document
        qrPairDoc.set(qrCodePair)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DEBUG","Successfully added QRPair");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "There was an issue with adding the QRpair",e);
                    }
                });
    }

    /**
     * Retrieve all QR pairs from Firestore.
     * @param onCompleteListener Listener to handle the completion of the retrieval operation
     */
    public void getQRpairs(OnCompleteListener<QuerySnapshot> onCompleteListener){
        ReusableQrCollection.get()
                .addOnCompleteListener( onCompleteListener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","Error with getting the QRpairs",e);
                    }
                });
    }

    /**
     * Delete a QR pair from Firestore.
     * @param eventID The ID of the event associated with the QR pair to delete
     */
    public void deleteQRPair(String eventID){
        DocumentReference qrPairDoc = ReusableQrCollection.document(eventID);
        qrPairDoc.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("DEBUG","successfully deleted a QR pair");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG","unsuccessfully deleted a QR pair");
            }
        });
    }
}
