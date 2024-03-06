package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CEventCQRFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_cqr, container, false);
        Button backButton = view.findViewById(R.id.BackButton);
        Button generateQRButton = view.findViewById(R.id.generateQRButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code for generating the QR code and pass it to the next fragment
                // place them here
                CEventPreviewFragment cEventPreviewFragment = new CEventPreviewFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, cEventPreviewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}