package com.example.seqr.helpers;

import android.graphics.Bitmap;

import androidx.core.content.FileProvider;

import com.example.seqr.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Allows for external sharing of files to the android built-in shareSheet widget
 * <p>
 *     Requires an XML with some private app data paths (i.e. ///this app///images/) and
 *     modifications to the app manifest to work properly.
 * <p>
 *     See <a href="https://developer.android.com/reference/androidx/core/content/FileProvider.html">https://developer.android.com/reference/androidx/core/content/FileProvider.html</a> for details.
 * </p>
 * @author Kyle Zwarich
 */

public class MyFileProvider extends FileProvider {
    public MyFileProvider(){
        super(R.xml.file_paths);
    }


}
