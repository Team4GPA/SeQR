package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CEventPreviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_preview, container, false);
        Button backButton = view.findViewById(R.id.BackButton);
        Button cEventPreviewCreateButton = view.findViewById(R.id.cEventPreviewCreateButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        cEventPreviewCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code for creating the object, place them somewhere here
                // code
                getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                CEventSuccessFragment cEventSuccessFragment = new CEventSuccessFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, cEventSuccessFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                //clear the fragment back stack
            }
        });
        return view;
    }
}