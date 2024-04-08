package com.example.seqr.helpers;

import android.net.Uri;

import com.example.seqr.database.Database;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A class responsible for uploading images to Firebase Storage.
 */
public class ImageUploader {
    private String dblocation;
    private String extension;
    private StorageReference mStorageRef;

    /**
     * Constructor for the ImageUploader class.
     *
     * @param dblocation The location in the Firebase Storage where the image will be stored.
     */
    public ImageUploader(String dblocation){
        this.dblocation = dblocation;
        mStorageRef = Database.getStorage().getReference(dblocation);

    }



    /**
     * Uploads the image to Firebase Storage.
     *
     * @param uri               The URI of the image to be uploaded.
     * @param storageConvention The storage convention for naming the image.
     */
    public void upload(Uri uri, String storageConvention){
        extension = "jpeg";

        if (uri != null) {
            StorageReference fileReference = mStorageRef.child(storageConvention+ "." + extension);
            fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
        else {
        }
    }


}
