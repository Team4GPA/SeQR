package com.example.seqr;

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

import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.ID;
import com.example.seqr.models.Profile;


public class StartUpFragment extends Fragment {



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
                    Profile newProfile = new Profile(username, uuid);
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