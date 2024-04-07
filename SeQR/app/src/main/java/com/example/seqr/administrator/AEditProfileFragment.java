package com.example.seqr.administrator;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seqr.MainActivity;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.database.Database;
import com.example.seqr.helpers.ImageUploader;
import com.example.seqr.models.ID;
import com.example.seqr.profile.DeleteProfileFragment;
import com.example.seqr.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A fragment for editing profiles in the admin dashboard.
 */
public class AEditProfileFragment extends Fragment{

    private FirebaseStorage storage;
    private EditText nameEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText homePageEditText;
    private ImageView profilePicView;
    private CheckBox isAdminCheckBox;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;

    public AEditProfileFragment() {
        storage = Database.getStorage();
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout of fragment and create buttons
        View view =  inflater.inflate(R.layout.fragment_a_edit_profile, container, false);
        Button deleteProfileButton = view.findViewById(R.id.admin_delete_profile_button);
        Button backButton = view.findViewById(R.id.admin_edit_profile_back_button);
        Button removeProfilePicButton = view.findViewById(R.id.admin_remove_profile_picture_button);
        Button submitChangesButton = view.findViewById(R.id.admin_edit_personal_info_button);
        Button editProfilePicButton = view.findViewById(R.id.admin_edit_profile_picture_button);

        ProfileController profileController = new ProfileController();
        // Unpack Bundle
        Bundle bundle = getArguments();
        assert bundle != null;
        String username = bundle.getString("username","");
        String email = bundle.getString("email","");
        String phoneNumber = bundle.getString("phoneNumber","");
        String homePage = bundle.getString("homePage","");
        String id = bundle.getString("id","");
        Boolean isAdmin = bundle.getBoolean("isAdmin",false);

        // Get specific fields to fill in
        nameEditText = view.findViewById(R.id.admin_profile_name);
        phoneNumberEditText = view.findViewById(R.id.admin_profile_number);
        emailEditText = view.findViewById(R.id.admin_profile_email);
        homePageEditText = view.findViewById(R.id.admin_profile_homepage);
        profilePicView = view.findViewById(R.id.admin_edit_profile_picture);

        // Set the fields to the values of the profile
        nameEditText.setText(username);
        emailEditText.setText(email);
        if (phoneNumber.equals("null")){
            phoneNumberEditText.setHint("Enter Phone Number");
        } else {
            phoneNumberEditText.setText(phoneNumber);
        }

        homePageEditText.setText(homePage);



        String path = Uri.encode("ProfilePictures/"+id+".jpg");
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Uri profilePicUri = Uri.parse(imageUrl);
        Picasso.get().load(profilePicUri).error(R.drawable.profile_picture_drawer_navigation_icon).into(profilePicView);

        // Handle pressing back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        editProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        removeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileController.deleteProfilePicture(id);

            }
        });

        submitChangesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isLegal = true;
                // Retrieve text entered in EditText views
                String username = nameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String homepage = homePageEditText.getText().toString();

                // Get the user's UUID
                String uuid = ID.getProfileId(getContext());

                if (username.isEmpty()){
                    nameEditText.setError("Please enter a username!");
                    isLegal = false;
                } else if (username.length() > 30) {
                    nameEditText.setError("Username must be less than 30 characters!");
                    isLegal = false;
                }

                if (phoneNumber.isEmpty()){
                    phoneNumberEditText.setError("Please enter a phone number");
                    isLegal = false;
                } else if (phoneNumber.length() > 30){
                    phoneNumberEditText.setError("Phone Number is too long!");
                    isLegal = false;
                }

                if (email.isEmpty()){
                    emailEditText.setError("Please enter an email");
                    isLegal = false;
                } else if (phoneNumber.length() > 30){
                    emailEditText.setError("Email is too long!");
                }

                if (homePage.isEmpty()){
                    homePageEditText.setError("Please enter a homepage");
                    isLegal = false;
                } else if (homepage.length() > 30){
                    homePageEditText.setError("Homepage is too long!");
                    isLegal = false;
                }

                // Update profile information in Firestore
                if (isLegal){
                    if (uuid != null) {
                        ProfileController profileController = new ProfileController();
                        profileController.updateProfile(uuid, username, phoneNumber, email, homepage);
                    }
                    getParentFragmentManager().popBackStack();
                }

            }
        });

        // Handle pressing delete button
        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("id", id);
                showConfirmationDialog(bundle1);
            }
        });

        return view;
    }

    /**
     * Shows the confirmation dialog for profile deletion.
     */
    private void showConfirmationDialog(Bundle bundle) {
        DeleteProfileFragment dialogFragment = new DeleteProfileFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getChildFragmentManager(), "DeleteProfileFragment");
    }


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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePicView.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(profilePicView);
            setProfilePicture(imageUri);
            ((MainActivity) getActivity()).setFirstTime(false);
        }
    }

    // Method to generate a new profile picture
    public void setProfilePicture(Uri imageUri) {
        // Update profile picture URL in Firestore
        String uuid = ID.getProfileId(getContext());
        if (uuid != null) {
            ImageUploader iuploader = new ImageUploader("ProfilePictures");
            iuploader.upload(imageUri, uuid);
            // Update stored URI in SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profile_picture_uri", imageUri.toString());
            editor.apply();
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
