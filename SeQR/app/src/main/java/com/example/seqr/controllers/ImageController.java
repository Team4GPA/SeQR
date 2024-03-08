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

public class ImageController {

    private FirebaseStorage storage;

    public ImageController(){
        storage = Database.getStorage();
    }

    public interface FileListCallback {
        void onSuccess(List<String> fileList);
        void onFailure(Exception e);
    }
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
