package com.example.seqr;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


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
        viewSignUpsButton =  view.findViewById(R.id.SignedUpButton);

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
        seteManagementBackButton();
        setViewSignUpsButton(bundle);

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


}

