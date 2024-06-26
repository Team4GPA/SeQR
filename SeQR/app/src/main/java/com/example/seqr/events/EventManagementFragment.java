package com.example.seqr.events;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.seqr.organizer.CheckInsFragment;
import com.example.seqr.qr.CQRFragment;
import com.example.seqr.qr.PQRFragment;
import com.example.seqr.R;
import com.example.seqr.organizer.SignUpsFragment;
import com.squareup.picasso.Picasso;

/**
 * A fragment for managing a specified event, including displaying QR codes, milestones, announcements, check-in map, attendance and users who signed up.
 */
public class EventManagementFragment extends Fragment {

    private ImageView eManagementPhoto;
    private Button cQRButton;
    private Button pQRButton;
    private Button eManagementBackButton;

    private Button viewSignUpsButton;
    private Button geoTrackButton;
    private Button viewCheckInsButton;

    private Button milestoneButton;

    private Button announcementListButton;

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_management, container, false);

        eManagementPhoto = view.findViewById(R.id.EManagementPhoto);
        cQRButton = view.findViewById(R.id.CQRButton);
        pQRButton = view.findViewById(R.id.PQRButton);
        eManagementBackButton = view.findViewById(R.id.EManagementBackButton);
        geoTrackButton = view.findViewById(R.id.GeoTrackingButton);

        viewSignUpsButton =  view.findViewById(R.id.SignedUpButton);
        viewCheckInsButton = view.findViewById(R.id.CheckedInButton);
        announcementListButton = view.findViewById(R.id.AnnouncementButton);
        milestoneButton = view.findViewById(R.id.MilestoneButton);


        Bundle bundle = getArguments();
        assert bundle != null;
        String eventName = bundle.getString("eventName", "");
        String checkInQR = bundle.getString("checkInQR", "");
        String promotionQR = bundle.getString("promotionQR", "");
        String eventId = bundle.getString("eventID","");

        TextView eManagementName = view.findViewById(R.id.EManagementName);
        eManagementName.setText(eventName);

        String path = Uri.encode("EventPosters/" + eventId + ".jpg");
        //this is the URL that the image is stored in firebase, picasso uses this to download the image
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Picasso.get().load(imageUrl).into(eManagementPhoto);

        setcQRButtonListener(bundle);
        setpQRButton(bundle);
        setAnnouncementListButton(bundle);
        seteManagementBackButton();
        setViewSignUpsButton(bundle);
        setMapButton(bundle);
        setViewCheckInsButton(bundle);
        setMilestoneButton(bundle);

        return view;
    }

    /**
     * Send user to checkin QRcode of the event
     * @param bundle data from the fragment
     */
    public void setcQRButtonListener(Bundle bundle){
        cQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CQRFragment cqrFragment = new CQRFragment();
                Bundle args = new Bundle();
                args.putString("checkInQR", bundle.getString("checkInQR"));
                args.putString("eventID", bundle.getString("eventID"));
                cqrFragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cqrFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Set the back button functionality
     * @author Kyle Zwarich
     *
     */
    public void seteManagementBackButton(){
        eManagementBackButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
    }

    /**
     * Send user to promotion QRcode of the event
     * @param bundle data from the fragment
     */
    public void setpQRButton(Bundle bundle){
        pQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PQRFragment pqrFragment = new PQRFragment();
                Bundle args = new Bundle();
                args.putString("promotionQR",bundle.getString("promotionQR"));
                args.putString("eventID", bundle.getString("eventID"));

                pqrFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pqrFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Method to handle choosing an announcement
     * @param bundle
     */
    public void setAnnouncementListButton(Bundle bundle) {
        announcementListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventAnnouncementListFragment eventAnnouncementListFragment = new EventAnnouncementListFragment();
                Bundle args = new Bundle();
                args.putString("eventID", bundle.getString("eventID"));
                args.putString("organizer", bundle.getString("organizer"));
                args.putBoolean("ifOrganizer", true);
                eventAnnouncementListFragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, eventAnnouncementListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Handles when a user wants to see who is signed up for an event
     * @param bundle
     */
    public void setViewSignUpsButton(Bundle bundle){
        viewSignUpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpsFragment signUpsFragment= new SignUpsFragment();
                Bundle args = new Bundle();
                args.putString("eventID",bundle.getString("eventID"));
                signUpsFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, signUpsFragment)
                        .addToBackStack(null)
                        .commit();


            }
        });
    }

    /**
     * Brings user to the event map fragment
     * @param bundle
     */
    public void setMapButton(Bundle bundle){
        geoTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMapFragment mapFrag = new EventMapFragment();
                mapFrag.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mapFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Brings user to see who is checked in to the event
     * @param bundle
     */
    public void setViewCheckInsButton(Bundle bundle){
        viewCheckInsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInsFragment checkInsFragment = new CheckInsFragment();
                Bundle args = new Bundle();
                args.putString("eventID",bundle.getString("eventID"));
                checkInsFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,checkInsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Brings user to set a milestone for the event
     * @param bundle
     */
    public void setMilestoneButton(Bundle bundle) {
        milestoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMilestoneFragment eventMilestoneFragment = new EventMilestoneFragment();
                Bundle args = new Bundle();
                args.putString("eventID", bundle.getString("eventID"));
                eventMilestoneFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, eventMilestoneFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}

