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
import com.example.seqr.helpers.ProfilePictureGenerator;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private final FirebaseStorage storage;
    private final Context context;

    /**
     * Constructs an Image controller and sets its storage to the storage from our database
     * @param theContext give context to the controller
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

    /**
     * Method that takes an image, checks what directory it is, and replaces it with a corresponding "placeholder" image
     *
     * @param imagePath string representing the path of the image in firebase storage
     */
    public void replaceImageWithPlaceHolder(String imagePath) {
        String[] directories = imagePath.split("/");
        String directory = directories[0];
        String image = directories[1];


        StorageReference storageRef = storage.getReference().child(imagePath);

        if (directory.equals("ProfilePictures")) {
            String[] parts = image.split("\\.");
            String userID = parts[0];
            // Replace with generate Profile Picture
            ProfileController profileController = new ProfileController();
            profileController.getProfileByUUID(userID, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document exists, retrieve username
                            String username = document.getString("username");
                            ProfilePictureGenerator profilePicGen = new ProfilePictureGenerator();
                            Bitmap bitmap = profilePicGen.generate(userID, username);
                            uploadBitmapImage(storageRef, bitmap);
                        }
                    }
                }
            });
        } else if (directory.equals("EventPosters")) {
            // Replace with event_icon.jpeg
            int resourceId = R.drawable.event_icon;
            uploadDrawableImage(storageRef, resourceId);
        } else {
            Log.d("DEBUG", "Unknown image type or invalid image path: " + imagePath);
        }
    }

    /**
     * uploads an image from the drawable folder and uploads it to firebase storage
     *
     * @param storageRef the place in storage to upload the image
     * @param resourceId an integer representing the R.id of the drawable image
     */
    private void uploadDrawableImage(StorageReference storageRef, int resourceId) {
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), resourceId);
        // Below is from tutorial: https://firebase.google.com/docs/storage/android/upload-files#java
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //compress images and resize to save space:
        //scale if larger than 800x800
        int targetLargestSide = 800;
        int currentWidth = bitmap.getWidth();
        int currentHeight = bitmap.getHeight();
        int newHeight = 0;
        int newWidth = 0;

        if ((currentWidth > targetLargestSide) || (currentHeight > targetLargestSide)){
            //cross-multiply:
            //knownWidth / knownHeight = targetWidth (800) / unknownHeight
            //(knownHeight x targetWidth) / knownWidth = unknownHeight;

            newHeight = (currentHeight * targetLargestSide) / currentWidth;
            float scaleFactor = newHeight/currentHeight;
            newWidth = (int) scaleFactor * currentWidth;

        }
        else{
            newHeight = currentHeight;
            newWidth = currentWidth;
        }

        Bitmap.Config currConfig = bitmap.getConfig();
        bitmap.reconfigure(newWidth, newHeight, currConfig);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 35, baos);


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

    /**
     * Converts a bitmap to an image and uploads the image to firebase storage
     * @param storageRef where the image should be uploaded in storage
     * @param bitmap a bitmap of the image to be uploaded
     */
    public void uploadBitmapImage(StorageReference storageRef, Bitmap bitmap){
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
