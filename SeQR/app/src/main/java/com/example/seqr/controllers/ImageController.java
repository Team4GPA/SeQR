package com.example.seqr.controllers;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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

    public void replaceImageWithPlaceHolder(String imagePath) {
        StorageReference imageReference = storage.getReference().child(imagePath);
        String defaultImageFile;

        // Get the Name of the directory, as the image we replace it with is dependent on what type of image it is
        String[] directories = imagePath.split("/");
        String directory = directories[0];

        if (directory.equals("ProfilePictures")) {
            defaultImageFile = "profile_picture.jpg";
        } else if (directory.equals("EventPosters")) {
            defaultImageFile = "event_poster.jpg";
        } else {
            Log.d("DEBUG", "Unknown image type or invalid image path: " + imagePath);
            return;
        }

        storage.getReference().child("PlaceHolders/" + defaultImageFile)
                        .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                       imageReference.putFile(uri)
                                               .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                   @Override
                                                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                       Log.d("DEBUG", "Image replace successfuly");
                                                   }
                                               })
                                               .addOnFailureListener(new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull Exception e) {
                                                       Log.d("Debug","Failed to replace image");
                                                   }
                                               });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "Couldn't retrieve image URL");
                    }
                });
    }
}
