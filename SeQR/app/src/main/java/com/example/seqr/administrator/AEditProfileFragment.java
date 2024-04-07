package com.example.seqr.administrator;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.seqr.controllers.ProfileController;
import com.example.seqr.database.Database;
import com.example.seqr.profile.DeleteProfileFragment;
import com.example.seqr.R;
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
        EditText nameEditText = view.findViewById(R.id.admin_profile_name);
        EditText phoneNumberEditText = view.findViewById(R.id.admin_profile_number);
        EditText emailEditText = view.findViewById(R.id.admin_profile_email);
        EditText homePageEditText = view.findViewById(R.id.admin_profile_homepage);
        ImageView profilePicView = view.findViewById(R.id.admin_edit_profile_picture);
        CheckBox isAdminCheckBox = view.findViewById(R.id.admin_edit_profile_admin_checkbox);

        // Set the fields to the values of the profile
        nameEditText.setText(username);
        emailEditText.setText(email);
        phoneNumberEditText.setText(phoneNumber);
        homePageEditText.setText(homePage);
        isAdminCheckBox.setChecked(isAdmin);

        String path = Uri.encode("ProfilePictures/"+id+".jpg");
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Uri profilePicUri = Uri.parse(imageUrl);
        Picasso.get().load(profilePicUri).error(R.drawable.profile_picture_drawer_navigation_icon).into(profilePicView);


        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("ProfilePictures/" +id +".jpg");
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String updatedImageUrl = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DEBUG", "Couldnt find profile picture" + databaseError);
            }
        });
        // Handle pressing back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        removeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileController.deleteProfilePicture(id);

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

}
