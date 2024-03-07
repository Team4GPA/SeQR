package com.example.seqr;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.seqr.database.Database;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//import io.grpc.Context;

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
