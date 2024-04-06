package com.example.seqr.controllers;

import android.util.Log;

import com.example.seqr.database.Database;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Controller class for managing Image data in Firestore database.
 */
public class ImageController {

    private FirebaseStorage storage;

    /**
     * Constructs an Image controller and sets its storage to the storage from our database
     */
    public ImageController(){
        storage = Database.getStorage();
    }

    /**
     * Interface for people to make sure they implement callBacks
     */
    public interface FileListCallback {
        void onSuccess(List<String> fileList);
        void onFailure(Exception e);
    }

    /**
     * returns an array with a list of strings with filenames from firebase storage based on the directory path given
     *
     * @param directoryPath string that represents where you want the function to search in the directory
     * @param callback return method to send the array back to the user who called it
     */
    public void getListOfFiles(String directoryPath, FileListCallback callback) {
        StorageReference storageRef = storage.getReference().child(directoryPath);
        // List all items in the directory
        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        // Iterate through each item and get its name
                        List<String> fileList = new ArrayList<>();
                        for (StorageReference item : listResult.getItems()) {
                            String fileName = item.getName();
                            fileList.add(fileName);
                        }
                        // Invoke the onSuccess callback with the list of file names
                        callback.onSuccess(fileList);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

}
