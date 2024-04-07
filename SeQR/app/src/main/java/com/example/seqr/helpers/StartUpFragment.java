package com.example.seqr.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.seqr.MainActivity;
import com.example.seqr.R;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.ID;
import com.example.seqr.models.Profile;
import com.example.seqr.profile.EditProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import com.example.seqr.helpers.ProfilePictureGenerator;
import com.example.seqr.helpers.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment represents startup screen
 */
public class StartUpFragment extends Fragment {

    private Uri imageUri;

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
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
                if (username.isEmpty()) {
                    username = "Guest";
                }
                ProfileController profileController = new ProfileController();
                String uuid = ID.createProfileID(getContext());
                List<String> notifications = new ArrayList<>();
                Profile newProfile = new Profile(username, uuid, notifications);
                ProfilePictureGenerator generator = new ProfilePictureGenerator();
                Bitmap newProfilePicture = generator.generate(ID.getProfileId(getContext()), username);
                imageUri = BitmapUtils.bitmapToUri(requireContext(), newProfilePicture);
                ((MainActivity) getActivity()).setFirstTime(false);
                ImageUploader iuploader = new ImageUploader("ProfilePictures");
                iuploader.upload(imageUri, uuid);
                // Update stored URI in SharedPreferences
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("profile_picture_uri", imageUri.toString());
                editor.apply();
                profileController.addProfile(newProfile);
                profileController.updateFCMToken(uuid);
                // Update profile picture URL in Firestore
                profileController.updatePFP(uuid, imageUri.toString(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Notify MainActivity about the profile picture update
                        final Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) activity).setImageUri(imageUri);
                                }
                            });
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "Error updating profile picture", e);
                    }
                });


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
        });
        return view;
    }
}