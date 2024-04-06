package com.example.seqr.organizer;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.seqr.R;
import com.example.seqr.controllers.EventController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AttendeeCheckInsView extends Fragment {
    private TextView usernameText;
    private ListView checkInsView;

    private ArrayAdapter<String> checkInsAdapter;
    private List<String> checkInsList = new ArrayList<>();
    private Button backButton;
    private ImageView profilePic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendee_check_ins_view, container, false);
        profilePic = view.findViewById(R.id.checkedIns_image);
        usernameText = view.findViewById(R.id.checkedIns_name);
        backButton = view.findViewById(R.id.attendeeCheckIns_back_button);
        checkInsView = view.findViewById(R.id.checkins_times_list);
        checkInsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,checkInsList);
        checkInsView.setAdapter(checkInsAdapter);
        Bundle bundle = getArguments();
        assert bundle != null;
        String profileID = bundle.getString("profileID");
        String username = bundle.getString("username");
        String eventID = bundle.getString("eventID");
        usernameText.setText(username);
        String path = Uri.encode("ProfilePictures/" + profileID + ".jpg");
        //this is the URL that the image is stored in firebase, picasso uses this to download the image
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Log.d("DEBUG",imageUrl);

        Picasso.get().load(imageUrl).error(R.drawable.profile_picture_drawer_navigation_icon).into(profilePic);


        displayUserCheckIns(profileID,eventID);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        return view;
    }

    public void displayUserCheckIns(String profileID, String eventID){
        EventController eventController = new EventController();
        eventController.getUserCheckIns(eventID, profileID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot userCheckInDoc = task.getResult();
                    if(userCheckInDoc != null && userCheckInDoc.exists()){
                        List<Timestamp> checkInTimes = (List<Timestamp>) userCheckInDoc.get("checkInTimes");
                        if (checkInTimes != null){
                            checkInsList.clear();
                            for(Timestamp timestamp: checkInTimes){
                                checkInsList.add(timestamp.toDate().toString());
                            }
                            checkInsAdapter.notifyDataSetChanged();
                        }
                    } else{
                        Log.d("DEBUG", "doc for user Check in doesnt exist");
                    }
                } else{
                    Log.d("DEBUG", "error in getting userCheckins");
                }
            }
        });

    }
}