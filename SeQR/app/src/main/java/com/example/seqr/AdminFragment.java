package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.checkerframework.checker.units.qual.A;

/**
 * A fragment representing the admin dashboard. Including browsing events, profiles and pictures.
 */
public class AdminFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin, container, false);
        Button backButton = view.findViewById(R.id.adminBackButton);
        Button browseEventsButton = view.findViewById(R.id.adminMainBrowseEvent);
        Button browseProfilesButton = view.findViewById(R.id.adminMainBrowseProfile);
        Button browseImagesButton = view.findViewById(R.id.adminMainBrowseImage);


        AEventFragment eventFrag = new AEventFragment();
        AImagesFragment imageFrag = new AImagesFragment();
        AProfilesFragment profileFrag = new AProfilesFragment();

        // setup fragment manager to get back to this admin screen if we press one of the browse buttons
        FragmentManager fragMgr = getFragmentManager();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        browseEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragMgr.beginTransaction().replace(R.id.fragment_container, eventFrag).addToBackStack(null).commit();
            }
        });

        browseProfilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragMgr.beginTransaction().replace(R.id.fragment_container, profileFrag).addToBackStack(null).commit();
            }
        });

        browseImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragMgr.beginTransaction().replace(R.id.fragment_container, imageFrag).addToBackStack(null).commit();
            }
        });
        return view;


    }
}