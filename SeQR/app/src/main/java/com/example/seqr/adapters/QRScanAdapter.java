package com.example.seqr.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

public class QRScanAdapter extends ViewModel {
    public String QRCodeResult;

    public void setQRCodeResult(String input) {
        this.QRCodeResult = input;
    }
    public String getQRCodeResult(){
        return this.QRCodeResult;
    }
}

