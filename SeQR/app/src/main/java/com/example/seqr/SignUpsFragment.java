package com.example.seqr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;


public class SignUpsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_ups, container, false);

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

        return view;
    }
}