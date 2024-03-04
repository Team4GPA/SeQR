package com.example.seqr;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    QRCodeGenerator test;
    Bitmap map;

    Button gen;

    ImageView dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gen = findViewById(R.id.tester);
        dis = findViewById(R.id.testdisplay);

        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "E0001";
                //map = test.generate(id);
                BitMatrix mMatrix;
                MultiFormatWriter mWriter = new MultiFormatWriter();
                BarcodeEncoder mEncoder = new BarcodeEncoder();
                try{
                    mMatrix = mWriter.encode(id, BarcodeFormat.QR_CODE, 400, 400);
                    map = mEncoder.createBitmap(mMatrix);
                    dis.setImageBitmap(map);
                    //displayQR(map);
                }
                catch(WriterException e){
                    e.printStackTrace();
                }
                //displayQR(map);
            }
        });

    }

    public void displayQR(Bitmap map){
        Dialog display_QR = new Dialog(MainActivity.this);
        display_QR.requestWindowFeature(Window.FEATURE_NO_TITLE);
        display_QR.setCancelable(true);
        display_QR.setContentView(R.layout.qrdisplay);

        ImageView qrdisplay = findViewById(R.id.QRdisplay);

        qrdisplay.setImageBitmap(map);

        display_QR.show();
    }
}