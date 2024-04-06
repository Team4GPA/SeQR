package com.example.seqr.organizer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.seqr.R;
import com.example.seqr.adapters.ProfileAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CheckInsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;

    private Button backButton;

    private TextView attendeeCountTextView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_ins, container, false);

        recyclerView = view.findViewById(R.id.CheckIns_profiles);
        profileList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(profileList, new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Profile profile) {
                Log.d("DEBUG","successfully clicked an profile signup");
                AttendeeCheckInsView attendeeCheckInsView = new AttendeeCheckInsView();
                Bundle bundle = getArguments();
                assert bundle != null;
                String eventID = bundle.getString("eventID","");
                Bundle args = new Bundle();
                args.putString("eventID",eventID);
                args.putString("profileID", profile.getId());
                args.putString("username",profile.getUsername());

                attendeeCheckInsView.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,attendeeCheckInsView)
                        .addToBackStack(null)
                        .commit();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(profileAdapter);

        Bundle bundle = getArguments();
        assert bundle != null;
        attendeeCountTextView = view.findViewById(R.id.textViewAttendeeTotal);
        attendeeCountTextView.setVisibility(View.GONE);
        backButton = view.findViewById(R.id.CheckIns_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        String eventID = bundle.getString("eventID","");
        //displayCheckins(eventID);
        displayCheckInsListener(eventID);

        return view;
    }

    // this was the code for just getting the checkIns, no snapshot listener.
//    public void displayCheckins(String eventID){
//        EventController eventController = new EventController();
//        ProfileController profileController = new ProfileController();
//        eventController.getEventCheckIns(eventID, new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    QuerySnapshot querySnapshot = task.getResult();
//                    if(!querySnapshot.isEmpty()){
//                        for (DocumentSnapshot checkInDoc: task.getResult()){
//                            String username = checkInDoc.getString("username");
//                            String profileID = checkInDoc.getId();
//                            if (isAdded()){
//                                attendeeCountTextView.setText(getString(R.string.number_of_attendees,querySnapshot.size()));;
//                                attendeeCountTextView.setVisibility(View.VISIBLE);
//                            }
//
//                            profileController.getProfileByUUID(profileID, new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        DocumentSnapshot profileDoc = task.getResult();
//                                        String username = profileDoc.getString("username");
//                                        String email = profileDoc.getString("email");
//                                        String phoneNumber = profileDoc.getString("phoneNumber");
//                                        String homePage = profileDoc.getString("homePage");
//                                        String id = profileDoc.getString("id");
//                                        String profilePic = profileDoc.getString("profilePic");
//                                        Profile profile = new Profile(username, email, phoneNumber, homePage, id, profilePic);
//                                        if (isAdded()){
//                                            profileList.add(profile);
//                                            profileAdapter.notifyDataSetChanged();
//                                        }
//
//                                    }else{
//                                        Log.d("DEBUG", "error in retrieving profile");
//                                    }
//                                }
//                            });
//
//                        }
//
//                    } else{
//                        if (isAdded()){
//                            attendeeCountTextView.setText(getString(R.string.number_of_attendees,0));
//                            attendeeCountTextView.setVisibility(View.VISIBLE);
//                        }
//
//                    }
//
//                } else {
//                    Log.d("DEBUG","error in getting checkIns");
//                }
//            }
//        });
//    }

    public void displayCheckInsListener(String eventID){
        EventController eventController = new EventController();
        ProfileController profileController = new ProfileController();

        eventController.eventCheckInsSnapshot(eventID, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.d("DEBUG","Error with listener for event checkIns:", error);
                    return;
                }
                if (!profileList.isEmpty()){
                    profileList.clear(); //remove the already existing profiles in the list so we dont get duplicates
                }
                if (value != null && !value.isEmpty()){
                    // set the count to the size of the snapshot, i.e number of people who checkedIn
                    if (isAdded()){
                        attendeeCountTextView.setText(getString(R.string.number_of_attendees, value.size()));
                        attendeeCountTextView.setVisibility(View.VISIBLE);
                    }

                    for (DocumentSnapshot checkedInUser : value.getDocuments()){
                        String username = checkedInUser.getString("username");
                        String profileID = checkedInUser.getId(); // gets the id of the document which we have set to profileIds
                        profileController.getProfileByUUID(profileID, new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot profileDoc = task.getResult();
                                    String username = profileDoc.getString("username");
                                    String email = profileDoc.getString("email");
                                    String phoneNumber = profileDoc.getString("phoneNumber");
                                    String homePage = profileDoc.getString("homePage");
                                    String id = profileDoc.getString("id");
                                    String profilePic = profileDoc.getString("profilePic");
                                    Profile profile = new Profile(username, email, phoneNumber, homePage, id, profilePic);
                                    if (isAdded()){
                                        if (!profileList.contains(profile)){
                                            // this is just another double check so there is no duplicates, there was a bug that was still causing dupes even though we cleared the list
                                            profileList.add(profile);
                                            profileAdapter.notifyDataSetChanged();
                                        }

                                    }

                                }else{
                                    Log.d("DEBUG", "error in retrieving profile");
                                }
                            }
                        });

                    }

                } else{
                    if (isAdded()){
                        attendeeCountTextView.setText(getString(R.string.number_of_attendees,0));
                        attendeeCountTextView.setVisibility(View.VISIBLE);
                    }

                }
            }

        });
    }
}