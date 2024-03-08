package com.example.seqr.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

/**
 * An adapter class for managing QR code scan results.
 */
public class QRScanAdapter extends ViewModel {
    public String QRCodeResult;


    /**
     * Sets the QR code result.
     *
     * @param input The QR code result to set.
     */
    public void setQRCodeResult(String input) {
        this.QRCodeResult = input;
    }

    /**
     * Gets the QR code result.
     *
     * @return The QR code result.
     */
    public String getQRCodeResult(){
        return this.QRCodeResult;
    }
}

