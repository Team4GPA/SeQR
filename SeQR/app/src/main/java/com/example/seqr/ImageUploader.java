package com.example.seqr;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//import io.grpc.Context;

public class ImageUploader {
    private String dblocation;
    private String extension;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    public ImageUploader(String dblocation){
        this.dblocation = dblocation;
        mStorageRef = FirebaseStorage.getInstance().getReference(dblocation);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Images");
    }

    public void upload(Uri uri){
        extension = "jpg";

        if (uri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + extension);
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
