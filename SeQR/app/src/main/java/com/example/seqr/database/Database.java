package com.example.seqr.database;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

/**
 * The database class is responsible for managing/connecting Firestore database and Firebase storage instances.
 */
public class Database {

    private static FirebaseFirestore db;

    private static FirebaseStorage storage;

    /**
     * constructor to initialize the database.
     */
    private Database(){

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

    }

    /**
     * Retrieves the Firebase storage instance.
     *
     * @return The FirebaseStorage instance.
     */
    public static FirebaseStorage getStorage(){
        //make sure storage is initialized
        if(storage == null){
            storage= FirebaseStorage.getInstance();
        }

        return storage;
    }

    /**
     * Retrieves the Firestore database instance.
     *
     * @return The Firebase database instance.
     */
    public static FirebaseFirestore getFireStore(){
        //make sure db is initialized
        if (db == null){
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }



}
