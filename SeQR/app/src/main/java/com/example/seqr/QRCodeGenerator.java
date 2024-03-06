package com.example.seqr;


import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeGenerator {
    BitMatrix mMatrix;
    MultiFormatWriter mWriter;
    BarcodeEncoder mEncoder;
    Bitmap mBitmap;

    public QRCodeGenerator() {
        mWriter = new MultiFormatWriter();
        mEncoder = new BarcodeEncoder();
    }

    public Bitmap generate(String event_id){
        try {
            mMatrix = mWriter.encode("test", BarcodeFormat.QR_CODE, 400, 400);
            mBitmap = mEncoder.createBitmap(mMatrix);
            return mBitmap;
        }
        catch (WriterException e){
            e.printStackTrace();
        }
        return mBitmap;
    }
}
