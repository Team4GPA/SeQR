package com.example.seqr.events.creation;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seqr.controllers.EventController;
import com.example.seqr.events.EventInfoFragment;
import com.example.seqr.qr.PQRFragment;
import com.example.seqr.R;
import com.squareup.picasso.Picasso;

/**
 * A fragment displayed upon successful creation of an event.
 */
public class CEventSuccessFragment extends Fragment {
    private TextView eventNameTextView;
    private TextView eventOrganizerTextView;
    private TextView eventLocationTextView;
    private TextView eventTimeTextView;
    private TextView eventCapacityTextView;
    private TextView eventDescriptionTextView;
    private ImageView eventImageView;
    private Button cEventSuccessGoToButton;

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
        View view = inflater.inflate(R.layout.fragment_c_event_success, container, false);

        eventOrganizerTextView = view.findViewById(R.id.cEventSuccessOrganizer);
        eventLocationTextView = view.findViewById(R.id.cEventSuccessLocation);
        eventTimeTextView = view.findViewById(R.id.cEventSuccessTime);
        eventCapacityTextView = view.findViewById(R.id.cEventSuccessCapacity);
        eventImageView = view.findViewById(R.id.photoPreview);
        cEventSuccessGoToButton = view.findViewById(R.id.cEventSuccessGoToButton);
        eventNameTextView = view.findViewById(R.id.cEventSuccessTitle);
        eventDescriptionTextView = view.findViewById(R.id.cEventSuccessDescription);
        Bundle bundle = getArguments();
        assert bundle != null;

        String eventName = bundle.getString("eventName", "");
        String organizerName = bundle.getString("organizerName","");
        String eventLocation = bundle.getString("eventLocation", "");
        String eventTime = bundle.getSerializable("eventTime").toString();
        String eventCapacity = bundle.getString("eventCapacity", "");
        String eventImageUriString = bundle.getString("imageUri", "");
        String promotionQR = bundle.getString("promotionQR","");
        String eventID = bundle.getString("eventID");
        String eventDescription = bundle.getString("eventDescription","");

        eventNameTextView.setText(eventName);
        eventOrganizerTextView.setText(organizerName);
        eventLocationTextView.setText(eventLocation);
        eventTimeTextView.setText(eventTime);
        eventDescriptionTextView.setText(eventDescription);

        if (!eventCapacity.contentEquals("-1")){
            eventCapacityTextView.setText(eventCapacity);
        }
        else {
            eventCapacityTextView.setText("No Capacity Limit");
        }

        //loads the image onto the event text
        Uri eventImageUri = Uri.parse(eventImageUriString);

        Picasso.get().load(eventImageUri).into(eventImageView);

        cEventSuccessGoToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("promotionQR",promotionQR);
                PQRFragment pqrFragment = new PQRFragment();
                pqrFragment.setArguments(bundle1);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pqrFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button viewEvent = view.findViewById(R.id.cEventSuccessViewEventButton);
        viewEvent.setOnClickListener(v -> {
            Bundle evID = new Bundle();
            evID.putString("eventID", eventID);
            EventInfoFragment succEvent = new EventInfoFragment();
            succEvent.setArguments(evID);
            FragmentManager frMgr = getParentFragmentManager();
            FragmentTransaction goToEvent = frMgr.beginTransaction();
            goToEvent.replace(R.id.fragment_container, succEvent);
            goToEvent.addToBackStack(null);
            goToEvent.commit();
        });

        return view;
    }
}