package com.example.seqr.database;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
public class Database {

    private static FirebaseFirestore db;

    private static FirebaseStorage storage;

    private Database(){

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

    }

    public static FirebaseStorage getStorage(){
        return storage;
    }

    public static FirebaseFirestore getFireStore(){
        //make sure db is initialized
        if (db == null){
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }



}
