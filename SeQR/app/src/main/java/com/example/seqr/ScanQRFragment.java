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

import com.example.seqr.adapters.QRScanAdapter;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

/**
 * ScanQRFragment
 * <p>
 * A fragment responsible for scanning QR codes using Google Play Services barcode scanner.s
 * This fragment should pop up a video window that shows a live feed for an Android
 * camera for scanning QR codes. It is currently a work in progress.
 * </p>
 * ** This fragment requires API 31 at minimum to access Google Play functionality. **
 * @author Kyle Zwarich
 * @version .1
 * <p>
 * Mar 4: replace camera with google play services barcode scanner to skip permissions
 * </p>
 */
public class ScanQRFragment extends Fragment {
    private String returnVal;
    private QRScanAdapter scanAdapter;
    private String DBTAG = "ScanQRFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DBTAG,"onCreate accessed.");

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
                    Log.d(DBTAG, "successful QR scan: " + barcode.getRawValue());
                    Toast.makeText(getContext(), "Scan successful!\n"+ barcode.getRawValue(), Toast.LENGTH_SHORT).show();
                    this.returnVal = barcode.getRawValue();

                    //use result listener:
                    //
                    //
                    Bundle result = new Bundle();
                    result.putString("gotQR", this.returnVal);

                    getParentFragmentManager().setFragmentResult("reqQR", result);
                    Log.d(DBTAG, "Sent bundle to FragmentResult with value " + result.getString("gotQR") + " and bundle is empty: " + result.isEmpty());
                })

                .addOnCanceledListener(()->
                {
                    //canceled
                    Bundle nullResult = new Bundle();
                    nullResult.putString("gotQR", "NULL");
                    getParentFragmentManager().setFragmentResult("reqQR", nullResult);
                    Log.d("ScanQR", "scan cancelled by user.");
                    Toast.makeText(getContext(), "Cancelled QR Code Scan!", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e ->{
                    //task failed with an exception
                    Log.d("ScanQR", "Exception: error on QR scan.");
                    Log.e("ScanQR", "Exception: ", e);
                });
    }
    /**
     * Retrieves the result of the QR code scan.
     *
     * @return The result of the QR code scan.
     */
    public String reportQRResult(){
        return this.returnVal;
    }
}

//update the scanAdapter:
//scanAdapter = new QRScanAdapter().get(QRScanAdapter.class);
//scanAdapter.setQRCodeResult(this.returnVal);
