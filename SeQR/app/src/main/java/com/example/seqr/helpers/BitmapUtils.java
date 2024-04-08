package com.example.seqr.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A helper class for handling Bitmap Operations
 */
public class BitmapUtils {
    /**
     * Converts a Bitmap image to a Uri.
     *
     * @param context The context to access resources or application-specific information.
     * @param bitmap The Bitmap image to be converted.
     * @return The Uri of the converted Bitmap image.
     */
    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        // Create a file to save the bitmap
        File file = new File(context.getCacheDir(), "image.jpg");

        try {
            // Write the bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(file);

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
                float scaleFactor = (float) newHeight /currentHeight;
                newWidth = Math.round(scaleFactor * currentWidth);

            }
            else{
                newHeight = currentHeight;
                newWidth = currentWidth;
            }
            Bitmap.Config currConfig = bitmap.getConfig();
            bitmap.reconfigure(newWidth, newHeight, currConfig);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 35, outputStream);

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