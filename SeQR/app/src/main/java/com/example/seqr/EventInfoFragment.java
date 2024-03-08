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
import com.example.seqr.models.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;


public class EventInfoFragment extends Fragment {

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
        String eventId = bundle.getString("eventId","");
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

        return view;

    }

    private void onSignUpPressed(String eventId){
        String userID = ID.getProfileId(getContext());
        ProfileController profileController = new ProfileController();
        profileController.getProfileUsernameByDeviceId(userID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null && doc.exists()){
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