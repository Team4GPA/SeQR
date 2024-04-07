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

public class ReusableQrController {
    private FirebaseFirestore db;
    private CollectionReference ReusableQrCollection;


    public ReusableQrController(){
        db = Database.getFireStore();
        ReusableQrCollection = db.collection("ReusableQR");
    }

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
}
