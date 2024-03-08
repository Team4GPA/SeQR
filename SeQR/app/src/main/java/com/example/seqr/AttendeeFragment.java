package com.example.seqr;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class AttendeeFragment extends Fragment {

    String qrResult;
    ScanQRFragment scanQRFragment = new ScanQRFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee, container, false);

        ExtendedFloatingActionButton floatingScanQRButton = view.findViewById(R.id.floatingScanQR);

        floatingScanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create a data bundle;
                Bundle qrResult = new Bundle();
                scanQRFragment.setArguments(qrResult); //send the bundle
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, scanQRFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        //get the results from fragment:
        this.qrResult = scanQRFragment.reportQRResult();
        Log.d("AttendeeFragment", "Got a QR code from scan: " + this.qrResult);
        //Toast.makeText(getContext(), "Got the QR code: \n " + this.qrResult , Toast.LENGTH_SHORT).show();

    }

}