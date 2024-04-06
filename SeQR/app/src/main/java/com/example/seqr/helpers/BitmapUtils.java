package com.example.seqr.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {
    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        // Create a file to save the bitmap
        File file = new File(context.getCacheDir(), "image.png");

        try {
            // Write the bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Create a Uri from the file
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            // Return null if an error occurs
            return null;
        }
    }
}
