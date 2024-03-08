package com.example.seqr;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seqr.models.Profile;
import com.example.seqr.adapters.ProfileAdapter;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Profile;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment showing all profiles for administrator.
 */
public class AProfilesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_profiles, container, false);

        // Create back the back button
        Button backButton = view.findViewById(R.id.admin_profiles_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){getParentFragmentManager().popBackStack();}
        });



        // Create recycler view
        recyclerView = view.findViewById(R.id.admin_profiles);
        profileList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(profileList);



        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(profileAdapter);

        // Query to get all profiles
        ProfileController profileController = new ProfileController();
        //comment this out to get a valid build

        /*
        profileController.getAllProfiles(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Profile profile = document.toObject(Profile.class);
                    profileList.add(profile);
                }
                profileAdapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG", "there was some error event in profile retrieval", task.getException());

            }
        });

         */

        return view;
    }
}