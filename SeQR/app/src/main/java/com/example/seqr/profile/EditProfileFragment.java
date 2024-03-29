package com.example.seqr.profile;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seqr.helpers.ImageUploader;
import com.example.seqr.MainActivity;
import com.example.seqr.R;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.ID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

/**
 * A fragment for editing profile information including profile picture, name, contact info and homepage.
 */
public class EditProfileFragment extends Fragment {

    EditText usernameEditText, phoneNumberEditText, emailEditText, homepageEditText;
    Button editPersonalInfoButton, editProfilePictureButton, editBackButton;
    private ImageView profileImageView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        // Initialize EditText views and button
        usernameEditText = view.findViewById(R.id.profile_name);
        phoneNumberEditText = view.findViewById(R.id.profile_number);
        emailEditText = view.findViewById(R.id.profile_email);
        homepageEditText = view.findViewById(R.id.profile_homepage);
        editPersonalInfoButton = view.findViewById(R.id.edit_personal_information);
        profileImageView = view.findViewById(R.id.photoPreview);
        editProfilePictureButton = view.findViewById(R.id.edit_profile_picture_button);
        editBackButton = view.findViewById(R.id.edit_back_button);


        ProfileController profileController = new ProfileController();

        String uuid = ID.getProfileId(getContext());

        String path = Uri.encode("ProfilePictures/" + uuid + ".jpg");
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Picasso.get().load(imageUrl).into(profileImageView);


        profileController.getProfileUsernameByDeviceId(uuid, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String user = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    String homepage = documentSnapshot.getString("homePage");
                    String phoneNumber = documentSnapshot.getString("phoneNumber"); // Retrieve phone number as an integer
                    usernameEditText.setText(user);
                    emailEditText.setText(email);
                    homepageEditText.setText(homepage);
                    phoneNumberEditText.setText(String.valueOf(phoneNumber));
                }
            }
        });



        editProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



        // Set OnClickListener for the button
        editPersonalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve text entered in EditText views
                String username = usernameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String homepage = homepageEditText.getText().toString();

                // Get the user's UUID
                String uuid = ID.getProfileId(getContext());

                // Update profile information in Firestore
                if (uuid != null) {
                    ProfileController profileController = new ProfileController();
                    profileController.updateProfile(uuid, username, phoneNumber, email, homepage);
                }
                getParentFragmentManager().popBackStack();
            }
        });

        //add listener to "back" button to pop the backstack and return to previous view.
        editBackButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
    //end of OnCreate
    //==============================================================================================

    /**
     * Opens a file chooser for selecting an image to upload as your profile picture.
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data (new profile picture) from it.
     */
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(profileImageView);
            String uuid = ID.getProfileId(getContext());
            ImageUploader iuploader = new ImageUploader("ProfilePictures");
            iuploader.upload(imageUri, uuid);

            // Update profile picture URL in Firestore
            ProfileController profileController = new ProfileController();
            profileController.updatePFP(uuid, imageUri.toString(), new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Notify MainActivity about the profile picture update
                    ((MainActivity) getActivity()).updateProfilePicture(imageUri);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("DEBUG", "Error updating profile picture", e);
                }
            });
        }
    }
}


