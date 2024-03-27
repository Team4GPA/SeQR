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
import android.widget.Toast;

import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Event;
import com.example.seqr.models.ID;
import com.example.seqr.models.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Event Info fragment shows the event details and has a button for signing up to an event
 * uses who scan a QR code a directed here as well as users who click on an event in event lobby
 */

public class EventInfoFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        TextView eventNameText = view.findViewById(R.id.eventInfoNameText);
        TextView eventOrganizer = view.findViewById(R.id.eventInfoOrganizer);
        TextView eventLocation = view.findViewById(R.id.eventInfoLocation);
        TextView eventTime = view.findViewById(R.id.eventInfoTime);
        TextView eventCapacity = view.findViewById(R.id.eventInfoCapacity);
        ImageView eventPhoto = view.findViewById(R.id.eventInfoPhotoPreview);
        TextView eventDescription = view.findViewById(R.id.eventInfoDescription);
        Button signUpButton = view.findViewById(R.id.signUpButtonEventInfo);

        Bundle bundle = getArguments();
        assert bundle != null;
        String eventId = bundle.getString("eventID","");
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpPressed(eventId);
                AttendeeFragment attendeeFragment = new AttendeeFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, attendeeFragment);
                fragmentTransaction.commit();

            }
        });
        EventController eventController = new EventController();
        eventController.getEventById(eventId,task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    Event event = document.toObject(Event.class);

                    eventNameText.setText(event.getEventName());
                    eventOrganizer.setText(event.getOrganizer());
                    eventLocation.setText(event.getLocation());
                    eventTime.setText(event.getEventStartTime());
                    eventCapacity.setText(String.valueOf(event.getMaxCapacity()));
                    eventDescription.setText(event.getEventDesc());


                    String path = Uri.encode("EventPosters/" + eventId + ".jpg");
                    String photoUri = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
                    Picasso.get().load(photoUri).into(eventPhoto);

                }else{
                    //for part 4 add a dialog that says event does not exist and redirect to main page
                    Log.d("Debug","There wasn't a document with that id");
                }
            }else{
                Log.d("Debug", "error in retrieveing the document/event");
            }
        });

        Button backButton = view.findViewById(R.id.backButtonEventInfo);
        backButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;

    }

    /**
     * When the signup button is pressed, get the Id and get the profile username using the id
     * after getting the username create a signup object with the username and id then call the event controller method for signing a user up
     * @param eventId this is the eventID
     */

    private void onSignUpPressed(String eventId){
        String userID = ID.getProfileId(getContext());
        ProfileController profileController = new ProfileController();
        profileController.getProfileUsernameByDeviceId(userID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null && doc.exists()){
                        List<String> signedUpEvents = (List<String>) doc.get("signedUpEvents");
                        //Check if the users signedUpEvents is null first i.e they havent signed up for anything yet
                        if(signedUpEvents == null){
                            signedUpEvents = new ArrayList<>();
                        }
                        // if they already signed up tell them
                        if (signedUpEvents.contains(eventId)){
                            // make sure context isn't null, was throwing errors before
                            if (getContext() != null){
                                Toast.makeText(getContext(), "Already signedup for this event", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            signedUpEvents.add(eventId);
                            profileController.signedUpEventsUpdater(userID,signedUpEvents);

                        }
                        String userName = doc.getString("username");

                        EventController eventController = new EventController();
                        SignUp signUp = new SignUp(userID, userName);
                        eventController.signUserUpForEvent(eventId,signUp);

                    }
                }else {
                    Log.d("DEBUG", "Error in getting the username");
                }
            }
        });

    }
}