package com.example.seqr.helpers;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Helper class to share images
 */
public class ShareImages {

    /**
     * A helper method to create a local temp file within the calling context's private app
     * data area.
     * @param currentContext Use getContext() to pass it through to this method.
     * @param prefix A string to define the prefix for the randomly generated temporary filename.
     * @param suffix The "file-type" for the image, usually ".png" or ".jpg"
     * @return The empty temporary file for use by the caller.
     * @author Kyle Zwarich
     */
    public File generateTempFile(Context currentContext, String prefix, String suffix){
        //Create some temporary storage
        File tempStorage = new File(currentContext.getFilesDir(), "temp");
        tempStorage.mkdir();
        tempStorage = new File(tempStorage.getAbsolutePath()+"/"+prefix+"_temp/");
        tempStorage.mkdir();
        File tempFile;
        try {
            tempFile = File.createTempFile(prefix, suffix, tempStorage);
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }

    /**
     * A helper method to compress an Android Bitmap into an image file for storage.
     * @param image The Bitmap to compress
     * @param emptyFile An empty, writable, accessible file within the app's private data storage.
     * @param pngORjpg 0 for png format, 1 for jpg format.
     * @throws IOException Fires an error if the write failed on the emptyFile
     * @author Kyle Zwarich
     */
    public void generateImageFile(Bitmap image, File emptyFile, int pngORjpg) throws IOException {
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        if(pngORjpg == 0){
            image.compress(Bitmap.CompressFormat.PNG, 0, imageBytes);
        }
        else if (pngORjpg == 1){
            image.compress(Bitmap.CompressFormat.JPEG, 20, imageBytes);
        }
        FileOutputStream writeBytes = new FileOutputStream(emptyFile);
        writeBytes.write(imageBytes.toByteArray());
        writeBytes.close();
    }
}
