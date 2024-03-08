package com.example.seqr;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Event;
import com.example.seqr.models.ID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

/**
 * A fragment for previewing event before creation.
 */
public class CEventPreviewFragment extends Fragment {
    private TextView eventNameTextView;
    private TextView eventOrganizerTextView;
    private TextView eventLocationTextView;
    private TextView eventTimeTextView;
    private TextView eventCapacityTextView;
    private TextView eventDescriptionTextView;
    private ImageView eventImageView;
    private Button cEventPreviewCreateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_preview, container, false);

        Button backButton = view.findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        eventNameTextView = view.findViewById(R.id.cEventPreviewName);
        eventOrganizerTextView = view.findViewById(R.id.cEventPreviewOrganizer);
        eventLocationTextView = view.findViewById(R.id.cEventPreviewLocation);
        eventTimeTextView = view.findViewById(R.id.cEventPreviewTime);
        eventCapacityTextView = view.findViewById(R.id.cEventPreviewCapacity);
        eventDescriptionTextView = view.findViewById(R.id.cEventPreviewDescription);
        eventImageView = view.findViewById(R.id.photoPreview);
        cEventPreviewCreateButton = view.findViewById(R.id.cEventPreviewCreateButton);

        Bundle bundle = getArguments();
        assert bundle != null;
        String organizerName = bundle.getString("organizerName","");
        String eventName = bundle.getString("eventName", "");
        String eventLocation = bundle.getString("eventLocation", "");
        String eventTime = bundle.getString("eventTime", "");
        String eventCapacity = bundle.getString("eventCapacity", "");
        String eventDescription = bundle.getString("eventDescription", "");
        String eventImageUriString = bundle.getString("imageUri", "");

        eventOrganizerTextView.setText(organizerName);
        eventNameTextView.setText(eventName);
        eventLocationTextView.setText(eventLocation);
        eventTimeTextView.setText(eventTime);
        eventCapacityTextView.setText(eventCapacity);
        eventDescriptionTextView.setText(eventDescription);

        //loads the image onto the event text
        Uri eventImageUri = Uri.parse(eventImageUriString);
        Picasso.get().load(eventImageUri).into(eventImageView);

        cEventPreviewCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for poping out all the previous fragments in the view hierarchy
                getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                String eventID = bundle.getString("eventID","");
                Uri imageUri = Uri.parse(eventImageUriString);
                ImageUploader iuploader = new ImageUploader("EventPosters");
                iuploader.upload(imageUri,eventID);
                String checkInQR = bundle.getString("checkInQR","");
                String promotionQR = bundle.getString("promotionQR","");
                String uuid = ID.getProfileId(getContext());
                Event event = new Event(eventName, eventID, eventDescription, Integer.parseInt(eventCapacity), organizerName, eventLocation, eventTime, promotionQR, checkInQR, uuid);

                EventController eventController = new EventController();
                eventController.addEvent(event);

                CEventSuccessFragment cEventSuccessFragment = new CEventSuccessFragment();
                cEventSuccessFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cEventSuccessFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}