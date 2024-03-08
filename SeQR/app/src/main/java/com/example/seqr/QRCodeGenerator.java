package com.example.seqr;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;


/**
 * A class for generating QR codes (both promotion and check-in) from event IDs.
 */
public class QRCodeGenerator {
    BitMatrix mMatrix;
    MultiFormatWriter mWriter;
    BarcodeEncoder mEncoder;
    Bitmap mBitmap;

    /**
     * Constructs a QRCodeGenerator object.
     */
    public QRCodeGenerator() {
        mWriter = new MultiFormatWriter();
        mEncoder = new BarcodeEncoder();
    }

    /**
     * Generates a QR code bitmap as a Base64-encoded string from the given event ID.
     *
     * @param event_id The event ID to encode in the QR code.
     * @return A Base64-encoded string representing the QR code bitmap.
     */
    //returns a string, when you want to display convert back into bitmap and then display in imageview
    public String generate(String event_id){
        try {
            mMatrix = mWriter.encode(event_id, BarcodeFormat.QR_CODE, 400, 400);
            mBitmap = mEncoder.createBitmap(mMatrix);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        }
        catch (WriterException e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Converts String representation of QRcode to Bitmap
     *
     * @param QRstring string representation of QRcode to be converted
     * @return bitmap bitmap representation of QRcode
     */
    public Bitmap convertToBitmap(String QRstring){
        try{
            byte [] encodeByte = Base64.decode(QRstring,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e){
            e.getMessage();
            return null;
        }
    }
}
