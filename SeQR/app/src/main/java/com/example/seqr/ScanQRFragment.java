package com.example.seqr;
//
// imports
//

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

/**
 * This fragment should pop up a video window that shows a live feed for an Android
 * camera for scanning QR codes. It is currently a work in progress.
 *
 * Mar 4: replace camera with google play services barcode scanner to skip permissions
 * -KZ
 */
public class ScanQRFragment extends Fragment {
    private GmsBarcodeScanner scanner;
    private String returnVal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ScanQR","onCreate accessed.");

        //Set up a Google-provided QR code scanner focusing on QR code inputs
        //also does some cool automatic capture and zooming
        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();

        //set up a new scanner instance;
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(this.getContext(), options);

        //start the scan asap;
        scanner.startScan()
                .addOnSuccessListener(barcode -> {
                    //successful
                    Log.d("ScanQR", "successful QR scan: " + barcode.getRawValue());
                    Toast.makeText(getContext(), "Scan successful!\n"+ barcode.getRawValue(), Toast.LENGTH_SHORT).show();
                    this.returnVal = barcode.getRawValue();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    Fragment event = new CEventDetailFragment();
                    Bundle eventID = new Bundle();
                    eventID.putString("QR", this.returnVal);
                    event.setArguments(eventID);
                    transaction.replace(R.id.fragment_container, event);
                    transaction.commit();
                })
                .addOnCanceledListener(()->
                {
                    //canceled
                    Toast.makeText(getContext(), "Cancelled QR Code Scan!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->{
                    //task failed with an exception
                    Log.d("ScanQR", "Exception: error on QR scan.");
                    Log.e("ScanQR", "Exception: ", e);
                });
    }

    public String reportQRResult(){
        return this.returnVal;
    }
}