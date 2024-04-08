package com.example.seqr.organizer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.seqr.adapters.ProfileAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Profile;
import com.example.seqr.models.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.seqr.R;


/**
 * Fragment that displays and handles all the attendees signed up for an event
 */
public class SignUpsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;


    /**
     * Method that creates the fragment to display and its associated logic
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the associated view of the fragment to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_ups, container, false);

        Button signupsBack = view.findViewById(R.id.signUps_back_button);

        recyclerView = view.findViewById(R.id.signUps_profiles);
        profileList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(profileList, new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Profile profile) {
                Log.d("DEBUG","successfully clicked an profile signup");
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(profileAdapter);

        Bundle bundle = getArguments();
        assert bundle != null;

        EventController eventController = new EventController();
        ProfileController profileController = new ProfileController();
        String eventID = bundle.getString("eventID","");
        eventController.getEventSignUps(eventID, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot signUpDoc : task.getResult()){
                        SignUp signUp = signUpDoc.toObject(SignUp.class);
                        if (signUp != null){
                            String id = signUp.getUserId();
                            profileController.getProfileByUUID(id, new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot profileDoc = task.getResult();
                                        String username = profileDoc.getString("username");
                                        String email = profileDoc.getString("email");
                                        String phoneNumber = profileDoc.getString("phoneNumber");
                                        String homePage = profileDoc.getString("homePage");
                                        String id = profileDoc.getString("id");
                                        String profilePic = profileDoc.getString("profilePic");
                                        Profile profile = new Profile(username, email, phoneNumber, homePage, id, profilePic);
                                        profileList.add(profile);
                                        Collections.sort(profileList, Comparator.comparing(Profile::getUsername));
                                        profileAdapter.notifyDataSetChanged();
                                    } else{
                                        Log.d("DEBUG", "error in retrieving profile");
                                    }
                                }
                            });
                        }


                    }
                }else{
                    Log.d("DEBUG", "retrieving the signup");
                }

            }
        });

        signupsBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });



        return view;
    }
}