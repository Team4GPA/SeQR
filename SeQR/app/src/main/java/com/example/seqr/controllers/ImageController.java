package com.example.seqr.controllers;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.seqr.R;
import com.example.seqr.database.Database;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Controller class for managing Image data in Firestore database.
 */
public class ImageController {

    private FirebaseStorage storage;
    private Context context;

    /**
     * Constructs an Image controller and sets its storage to the storage from our database
     */
    public ImageController(Context theContext){
        storage = Database.getStorage();
        context = theContext;
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
        String[] directories = imagePath.split("/");
        String directory = directories[0];

        StorageReference storageRef = storage.getReference().child(imagePath);

        if (directory.equals("ProfilePictures")) {
            // Replace with profile_picture.jpg
            int resourceId = R.drawable.profile_picture;
            uploadDrawableImage(storageRef, resourceId);
        } else if (directory.equals("EventPosters")) {
            // Replace with event_icon.jpg
            int resourceId = R.drawable.event_icon;
            uploadDrawableImage(storageRef, resourceId);
        } else {
            Log.d("DEBUG", "Unknown image type or invalid image path: " + imagePath);
        }
    }

    private void uploadDrawableImage(StorageReference storageRef, int resourceId) {
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), resourceId);
        // Below is from tutorial: https://firebase.google.com/docs/storage/android/upload-files#java
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("DEBUG", "Image failed to upload: " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("DEBUG", "Image uploaded successfully: " +taskSnapshot);
            }
        });
    }
}
