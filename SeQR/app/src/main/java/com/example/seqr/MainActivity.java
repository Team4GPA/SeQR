package com.example.seqr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.seqr.controllers.ProfileController;
import com.example.seqr.database.Database;
import com.example.seqr.models.ID;
import com.example.seqr.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;


/**
 * MainActivity represents the main activity of the application. It handles both the top and bottom navigation bars,
 * profile management, and displays various fragments such as lobby, attendee and organizer based on user interactions.
 */
public class MainActivity extends AppCompatActivity {

    private AttendeeFragment attendeeFragment = new AttendeeFragment();
    private EventLobbyFragment eventLobbyFragment = new EventLobbyFragment();
    private OrganizerFragment organizerFragment = new OrganizerFragment();

    private ImageView profileImageView;

    /**
     * Called when the activity is starting. Initializes the main activity layout and sets up
     * various UI components and listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *      stopped then this Bundle contains the data it most recently supplied in
     *      onSaveInstanceState(Bundle).
     */
    private StartUpFragment startUpFragment = new StartUpFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String uuid = ID.getProfileId(getBaseContext());


        if (uuid == null) {
            FragmentManager fragMgr = getSupportFragmentManager();
            View mainFrag = findViewById(R.id.fragment_container);
            // set the page to attendee view as initialization
            fragMgr.beginTransaction().replace(R.id.fragment_container, startUpFragment).commit();

        } else {


            // initialize buttons for the side menu
            Button editProfileButton = findViewById(R.id.edit_profile_button);
            CheckBox enableGeoLocationCheckbox = findViewById(R.id.enable_geo_location_checkbox);
            ProfileController profileController = new ProfileController();

            profileController.getProfileUsernameByDeviceId(uuid, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            boolean geoLocationEnabled = documentSnapshot.getBoolean("geoLocation");
                            // Update the checkbox state based on the retrieved value
                            enableGeoLocationCheckbox.setChecked(geoLocationEnabled);
                        }
                    }
                }
            });

            // Add an event listener to the checkbox
            enableGeoLocationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Update the geolocation setting in Firestore for the current user
                    String uuid = ID.getProfileId(MainActivity.this);
                    if (uuid != null) {
                        ProfileController profileController = new ProfileController();
                        profileController.updateGeoLocation(uuid, isChecked);
                    }
                }
            });
            Button adminButton = findViewById(R.id.admin_button);

            profileImageView = findViewById(R.id.profile_picture);


            String path = Uri.encode("ProfilePictures/" + uuid + ".jpg");
            String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
            Picasso.get().load(imageUrl).into(profileImageView);


            //for testing: add a floating QR button over the main fragment view 'fragment_container'
//        ExtendedFloatingActionButton qrButton = findViewById(R.id.scanQRButton);
//        String qrResult;

            //setup the main fragment view stuff
            FragmentManager fragMgr = getSupportFragmentManager();
            View mainFrag = findViewById(R.id.fragment_container);
            // set the page to attendee view as initialization
            fragMgr.beginTransaction().replace(R.id.fragment_container, attendeeFragment).commit();

            //sliding drawer layout on left-hand side of Activity window
            DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout);

            // Handle on clicks for bottom navigation bar
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

            bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.bottom_attendee) {
                        // Handle attendee button, popBackStack to empty the fragment back stack.
                        fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragMgr.beginTransaction().replace(R.id.fragment_container, attendeeFragment).commit();
                        return true;
                    } else if (id == R.id.bottom_organizer) {
                        // Handle organizer button
                        fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragMgr.beginTransaction().replace(R.id.fragment_container, organizerFragment).commit();
                        return true;
                    } else if (id == R.id.bottom_events) {
                        // Handle events button
                        fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragMgr.beginTransaction().replace(R.id.fragment_container, eventLobbyFragment).commit();
                        return true;
                    }

                    return false;
                }
            });

            Toolbar toolbar = findViewById(R.id.toolbar);

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.notification_icon) {
                        // Handle notification icon click
                        return true;
                    }
                    return false;
                }
            });

            profileController.getProfileDocument(uuid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("MainActivity", "Error listening to profile document changes", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String user = documentSnapshot.getString("username");
                        // Update the profile name TextView
                        TextView profileNameView = findViewById(R.id.profile_name_textview);
                        profileNameView.setText(user);
                    }
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle navigation icon click
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });

            //handle clicks on the QR code button;
            //uses some variables set up earlier:
            //mainFrag: a view in the XML for the main_activity (acts as a container for fragments)
            //fragMgr: Android manager of fragments
            //
//        qrButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//           public void onClick(View v) {
//                FragmentTransaction swapMain = fragMgr.beginTransaction();
//                Fragment cameraFrag = new ScanQRFragment();
//                swapMain.replace(mainFrag.getId(), cameraFrag);
//                swapMain.commit();
//            }
//        });

            adminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdminFragment adminFragment = new AdminFragment();
                    FragmentTransaction transaction = fragMgr.beginTransaction();
                    transaction.replace(R.id.fragment_container, adminFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }
            });
            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditProfileFragment editprofileFragment = new EditProfileFragment();
                    FragmentTransaction transaction = fragMgr.beginTransaction();
                    transaction.replace(R.id.fragment_container, editprofileFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }
            });
        }
    }
//
//    //==============================================================================================
//    //End of onCreate() override
//    //
//
//    //==============================================================================================
//    //Custom Methods
//    //
//    private void startUpLogic(){
//        setContentView(R.layout.start_up);
//        EditText userNameEntry = findViewById(R.id.enteredUsername);
//        Button confirmButton = findViewById(R.id.createProfileConfirmButton);
//        Context thisContext = this;
//
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("DEBUG","Onclick reached");
//                // call profile controller we are about to add a profile
//                ProfileController profileController = new ProfileController();
//                String username =  userNameEntry.getText().toString();
//                String uuid = ID.createProfileID(thisContext);
//                Profile newProfile = new Profile(username, uuid);
//                profileController.addProfile(newProfile);
//                setContentView(R.layout.activity_main);
//            }
//        });
//    }
//}


    //==============================================================================================
    //Custom Methods
    //
    /**
     * Initializes the startup logic for the application, which includes displaying the initial
     * layout for creating a user profile (for first time user).
     */
//    private void startUpLogic(){
//        setContentView(R.layout.start_up);
//        EditText userNameEntry = findViewById(R.id.enteredUsername);
//        Button confirmButton = findViewById(R.id.createProfileConfirmButton);
//        Context thisContext = this;
//
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("DEBUG","Onclick reached");
//                // call profile controller we are about to add a profile
//                ProfileController profileController = new ProfileController();
//                String username =  userNameEntry.getText().toString();
//                String uuid = ID.createProfileID(thisContext);
//                Profile newProfile = new Profile(username, uuid);
//                profileController.addProfile(newProfile);
//                setContentView(R.layout.activity_main);
//            }
//        });
//    }

    /**
     * Updates the profile picture displayed in the profile image view with the provided image URI.
     *
     * @param imageUri The URI of the profile picture to be displayed.
     */
    public void updateProfilePicture(Uri imageUri) {
        Picasso.get().load(imageUri).into(profileImageView);
    }
    //==============================================================================================
    //End of MainActivity Class
    //
}