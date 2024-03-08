package com.example.seqr;

import android.net.Uri;

import com.example.seqr.database.Database;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploader {
    private String dblocation;
    private String extension;
    private StorageReference mStorageRef;


    public ImageUploader(String dblocation){
        this.dblocation = dblocation;
        mStorageRef = Database.getStorage().getReference(dblocation);

    }



    public void upload(Uri uri, String storageConvention){
        extension = "jpg";

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
