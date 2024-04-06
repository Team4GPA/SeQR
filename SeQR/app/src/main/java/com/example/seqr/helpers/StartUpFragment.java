package com.example.seqr.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seqr.MainActivity;
import com.example.seqr.R;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.ID;
import com.example.seqr.models.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment represents startup screen
 */
public class StartUpFragment extends Fragment {


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
        View view = inflater.inflate(R.layout.fragment_start_up, container, false);

        EditText userNameEnter = view.findViewById(R.id.enteredUsername);
        Button confirmButton = view.findViewById(R.id.createProfileConfirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameEnter.getText().toString();
                if (username.isEmpty()){
                    Toast.makeText(getContext(), "Cant have an empty Username", Toast.LENGTH_SHORT).show();
                } else{
                    ProfileController profileController = new ProfileController();
                    String uuid = ID.createProfileID(getContext());
                    List<String> notifications = new ArrayList<>();
                    Profile newProfile = new Profile(username, uuid, notifications);
                    profileController.addProfile(newProfile);

                    //Restart the Main Activity so it loads all the buttons/click listeners and data.
                    Activity activity = getActivity();
                    if (activity != null) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        // Clear out old activty and make a new one
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        activity.finish();
                    }


                }

            }
        });
        return view;
    }
}