package com.example.seqr.administrator;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seqr.R;
import com.example.seqr.models.Profile;
import com.example.seqr.adapters.ProfileAdapter;
import com.example.seqr.controllers.ProfileController;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment showing all profiles for administrator.
 */
public class AProfilesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;

    /**
     * Creates a view and associated logic for the view and returns it to whoever built the fragment
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

        FragmentManager frgMgr = getParentFragmentManager();
        profileAdapter = new ProfileAdapter(profileList, new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Profile profile) {
                Bundle bundle = new Bundle();
                bundle.putString("username", profile.getUsername());
                bundle.putString("email", profile.getEmail());
                bundle.putString("phoneNumber", profile.getPhoneNumber());
                bundle.putString("homePage", profile.getHomePage());
                bundle.putString("id", profile.getId());
                bundle.putBoolean("isAdmin",profile.isAdmin());
                AEditProfileFragment editProfileFragment = new AEditProfileFragment();
                editProfileFragment.setArguments(bundle);
                frgMgr.beginTransaction().replace(R.id.fragment_container, editProfileFragment).addToBackStack(null).commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(profileAdapter);

        // Query to get all profiles
        ProfileController profileController = new ProfileController();
        profileController.getAllProfiles(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String username = document.getString("username");
                    String email = document.getString("email");
                    String phoneNumber = document.getString("phoneNumber");
                    String homePage = document.getString("homePage");
                    String id = document.getString("id");
                    String profilePic = document.getString("profilePic");
                    Profile profile = new Profile(username, email, phoneNumber, homePage, id, profilePic);
                    profileList.add(profile);
                }
                Collections.sort(profileList, Comparator.comparing(Profile::getUsername));
                profileAdapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG", "there was some error event in profile retrieval", task.getException());
            }
        });



        return view;
    }
}