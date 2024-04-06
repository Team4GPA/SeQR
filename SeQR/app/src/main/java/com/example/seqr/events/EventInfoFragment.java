package com.example.seqr.events;

import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seqr.R;
import com.example.seqr.attendee.AttendeeFragment;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Event;
import com.example.seqr.models.ID;
import com.example.seqr.models.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.ArrayList;
import java.util.List;

/**
 * Event Info fragment shows the event details and has a button for signing up to an event
 * uses who scan a QR code a directed here as well as users who click on an event in event lobby
 */

public class EventInfoFragment extends Fragment {
    private ImageButton eventInfoAnnouncementButton;

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
        eventInfoAnnouncementButton = view.findViewById(R.id.eventInfoAnnouncementButton);

        Bundle bundle = getArguments();
        assert bundle != null;
        String eventId = bundle.getString("eventID","");
        Log.d("DEBUG", "Event ID :" + eventId);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpPressed(eventId, getContext());
                //wait for confirmation before continuing!

                Toast.makeText(getContext(), "Added \""+eventNameText.getText().toString()+"\" to your collection!", Toast.LENGTH_SHORT).show();
                BottomNavigationView bnav = getActivity().findViewById(R.id.bottom_nav);
                FragmentManager frgMgr = getParentFragmentManager();
                FragmentTransaction trans = frgMgr.beginTransaction();
                trans.addToBackStack(null);
                trans.commit();
                bnav.setSelectedItemId(R.id.bottom_attendee);

//                AttendeeFragment attendeeFragment = new AttendeeFragment();
//                FragmentManager fragmentManager = getParentFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, attendeeFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelSignUpEventInfo);
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setOnClickListener(v -> {
            onCancelSignUp(eventId, getContext());
            AttendeeFragment attendeeFragment = new AttendeeFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fire = fragmentManager.beginTransaction();
            fire.replace(R.id.fragment_container, attendeeFragment);
            fire.addToBackStack(null);
            fire.commit();
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
                    if (event.getMaxCapacity() == -1){
                        eventCapacity.setText("No Capacity Limit");
                    }
                    else {
                        eventCapacity.setText(String.valueOf(event.getMaxCapacity()));
                    }
                    eventDescription.setText(event.getEventDesc());

                    ProfileController quickCheck = new ProfileController();
                    String userID = ID.getProfileId(getContext());
                    quickCheck.getProfileUsernameByDeviceId(userID, new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot userProfile = task.getResult();
                                List<String> usersEvents = (List<String>) userProfile.get("signedUpEvents");
                                if (usersEvents != null){
                                    if (usersEvents.contains(event.getEventID())){
                                        cancelButton.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        cancelButton.setVisibility(View.INVISIBLE);
                                    }
                                }

                            }
                            else{
                                Log.d("EVENTINFO", "Failed to fetch document from firebase.");
                            }
                        }
                    });



                    String path = Uri.encode("EventPosters/" + eventId + ".jpg");
                    String photoUri = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
                    Picasso.get().load(photoUri).into(eventPhoto);

                }else{
                    //for part 4 add a dialog that says event does not exist and redirect to main page
                    Log.d("Debug","There wasn't a document with that id");
                }
            }else{
                Log.d("Debug", "error in retrieving the document/event");
            }
        });

        Button backButton = view.findViewById(R.id.backButtonEventInfo);
        backButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        setEventInfoAnnouncementButton(bundle);

        return view;

    }

    /**
     * When the signup button is pressed, get the Id and get the profile username using the id
     * after getting the username create a signup object with the username and id then call the event controller method for signing a user up
     *
     * @param eventId this is the eventID
     * @param context
     */

    private void onSignUpPressed(String eventId, Context context){
        String userID = ID.getProfileId(context);
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
                            if (context != null){
                                Toast.makeText(context, "Already signed up for this event!", Toast.LENGTH_SHORT).show();
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

    /**
     * Allow the user to cancel their sign up if they are already signed up for the event.
     * @param eventID The eventid of the currently viewing event
     * @param context The parent context
     */
    public void onCancelSignUp(String eventID, Context context){
        String userID = ID.getProfileId(context);
        ProfileController profileController = new ProfileController();
        profileController.getProfileUsernameByDeviceId(userID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null && doc.exists()){
                        List<String> signedUpEvents = (List<String>) doc.get("signedUpEvents");
                        if (signedUpEvents.contains(eventID)){
                            // make sure context isn't null, was throwing errors before
                            if (context != null){
                                signedUpEvents.remove(eventID);
                                profileController.signedUpEventsUpdater(userID, signedUpEvents);
                                EventController eventController = new EventController();
                                SignUp signUp = new SignUp(userID, null);
                                eventController.cancelSignUpForEvent(eventID,signUp);
                                Toast.makeText(context, "Removed this event from your collection!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //should never see this...
                            Toast.makeText(context, "Somehow this event is not in your collection...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Log.d("DEBUG", "Error in getting the username");
                }
            }
        });
    }

    public void setEventInfoAnnouncementButton(Bundle bundle) {
        eventInfoAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventAnnouncementListFragment eventAnnouncementListFragment = new EventAnnouncementListFragment();
                Bundle args = new Bundle();
                args.putString("eventID", bundle.getString("eventID"));
                args.putBoolean("ifOrganizer", false);
                eventAnnouncementListFragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, eventAnnouncementListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}