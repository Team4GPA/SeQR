package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class AttendeeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee, container, false);

        ExtendedFloatingActionButton floatingScanQRButton = view.findViewById(R.id.floatingScanQR);
        String qrResult;

        floatingScanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanQRFragment scanQRFragment = new ScanQRFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, scanQRFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}