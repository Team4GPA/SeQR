package com.example.seqr.profile;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.media.ImageReader;
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

import com.example.seqr.helpers.BitmapUtils;
import com.example.seqr.helpers.ImageUploader;
import com.example.seqr.MainActivity;
import com.example.seqr.R;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.helpers.ShareImages;
import com.example.seqr.models.ID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.example.seqr.helpers.ProfilePictureGenerator;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Objects;

/**
 * A fragment for editing profile information including profile picture, name, contact info and homepage.
 */
public class EditProfileFragment extends Fragment {

    EditText usernameEditText, phoneNumberEditText, emailEditText, homepageEditText;
    Button editPersonalInfoButton, editProfilePictureButton, editBackButton, removeProfilePictureButton;
    private ImageView profileImageView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Uri bitmapUri;
    private boolean firstTime = true;
    /**
     * Inflates the layout for the edit profile fragment.
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
        removeProfilePictureButton = view.findViewById(R.id.remove_profile_picture_button);

        loadProfilePicture();

        ProfileController profileController = new ProfileController();

        String uuid = ID.getProfileId(getContext());

        profileController.getProfileUsernameByDeviceId(uuid, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String user = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    String homepage = documentSnapshot.getString("homePage");
                    String phoneNumber = documentSnapshot.getString("phoneNumber"); // Retrieve phone number as an integer
                    usernameEditText.setText(user);
                    emailEditText.setText(email);
                    homepageEditText.setText(homepage);
                    if (phoneNumber.equals("null")){
                        phoneNumberEditText.setHint("Enter Phone Number");
                    }else{
                        phoneNumberEditText.setText(String.valueOf(phoneNumber));
                    }

                }
            }
        });

        removeProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePictureGenerator generator = new ProfilePictureGenerator();
                Bitmap newProfilePicture = generator.generate(ID.getProfileId(getContext()), usernameEditText.getText().toString());
                profileImageView.setImageBitmap(newProfilePicture);
                bitmapUri = BitmapUtils.bitmapToUri(requireContext(), newProfilePicture);
                // Call the method to generate a new profile picture and update the profile picture accordingly
                setProfilePicture(bitmapUri);
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

    /**
     * Loads the profile picture either from SharedPreferences or from Firebase Storage
     * based on whether it's the first time loading or not.
     */
    private void loadProfilePicture() {
        String storedUri = getStoredProfilePictureUri();

        Bundle args = getArguments();
        firstTime = args != null && args.getBoolean("first_time", true); // Default value is true

        if (!firstTime && isValidUri(storedUri)) {
            Picasso.get().load(storedUri).into(profileImageView);
        } else {
            String uuid = ID.getProfileId(getContext());
            String path = Uri.encode("ProfilePictures/" + uuid + ".jpg");
            String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
            Picasso.get().load(imageUrl).into(profileImageView);
        }
    }
    /**
     * Retrieves the stored profile picture URI from SharedPreferences.
     *
     * @return The stored profile picture URI.
     */
    private String getStoredProfilePictureUri() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
        return sharedPreferences.getString("profile_picture_uri", null);
    }
    /**
     * Checks if the given URI string is valid.
     *
     * @param uriString The URI string to check.
     * @return True if the URI string is valid, false otherwise.
     */
    private boolean isValidUri(String uriString) {
        if (uriString == null || uriString.isEmpty()) {
            return false;
        }

        try {
            Uri uri = Uri.parse(uriString);
            return uri != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method to set the profile Picture
     * @param imageUri
     */
    public void setProfilePicture(Uri imageUri) {

        System.out.println("SUCCESSFUL ACCESS");
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


    /**
     * Opens a file chooser for selecting an image to upload as your profile picture.
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).showToolbar(); // Notify MainActivity to show the toolbar
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data (new profile picture) from it.
     */
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            //start decoding and compressing input images.
            Log.d("EDIT PROFILE", "Data is " + data.getData());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                ImageDecoder.Source usrSrc = ImageDecoder.createSource(getContext().getContentResolver(), imageUri);
                try {
                    Bitmap bitmap = ImageDecoder.decodeBitmap(usrSrc, new ImageDecoder.OnHeaderDecodedListener() {
                        @Override
                        public void onHeaderDecoded(@NonNull ImageDecoder decoder, @NonNull ImageDecoder.ImageInfo info, @NonNull ImageDecoder.Source source) {
                            int targetLargestSide = 800;
                            int currentWidth = info.getSize().getWidth();
                            Log.d("BITMAP PROC", "Current width is " + currentWidth);
                            int currentHeight = info.getSize().getHeight();
                            Log.d("BITMAP PROC", "Current height is " + currentHeight);
                            int newHeight = 0;
                            int newWidth = 0;

                            if ((currentWidth > targetLargestSide) || (currentHeight > targetLargestSide)){
                                //cross-multiply:
                                //knownWidth / knownHeight = targetWidth (800) / unknownHeight
                                //(knownHeight x targetWidth) / knownWidth = unknownHeight;

                                newHeight = (currentHeight * targetLargestSide) / currentWidth;
                                float scaleFactor = (float) newHeight /currentHeight;
                                float fnewWidth = (float) (scaleFactor * currentWidth);
                                newWidth = Math.round(fnewWidth);
                            }
                            else {
                                newHeight = currentHeight;
                                newWidth = currentWidth;
                            }

                            Log.d("NEW BITMAP", "New height is " + newHeight + " and width is " + newWidth);
                            decoder.setTargetSize(newWidth, newHeight);
                        }
                    });
                    if (bitmap == null){
                        Log.d("EDIT PROFILE", "Error: null reference on the bitmap (could not convert chosen file)");
                    }

                    //write the config'd bitmap first to temp file:
                    ByteArrayOutputStream dataWrite = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 35, dataWrite);
                    ShareImages imgHelper = new ShareImages();
                    File tempFile = imgHelper.generateTempFile(getContext(), "usr_pick", ".tmp");
                    tempFile.deleteOnExit();
                    Uri newImg;
                    try {
                        FileOutputStream fileWrite = new FileOutputStream(tempFile);
                        fileWrite.write(dataWrite.toByteArray());
                        fileWrite.close();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //give the new Uri to the next steps
                    newImg = getUriForFile(getContext(), "com.example.seqr", tempFile);

                    profileImageView.setImageURI(newImg);
                    Picasso.get().load(newImg).into(profileImageView);
                    setProfilePicture(newImg);
                    ((MainActivity) getActivity()).setFirstTime(false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }


        }
    }

}