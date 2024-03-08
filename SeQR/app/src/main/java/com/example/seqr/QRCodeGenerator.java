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

public class QRCodeGenerator {
    BitMatrix mMatrix;
    MultiFormatWriter mWriter;
    BarcodeEncoder mEncoder;
    Bitmap mBitmap;

    public QRCodeGenerator() {
        mWriter = new MultiFormatWriter();
        mEncoder = new BarcodeEncoder();
    }

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
