package com.example.seqr;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.seqr.administrator.AdminFragment;
import com.example.seqr.administrator.EnterPasswordFragment;
import com.example.seqr.announcements.AnnouncementDetailFragment;
import com.example.seqr.attendee.AttendeeFragment;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.events.EventLobbyFragment;
import com.example.seqr.events.EventMilestoneFragment;
import com.example.seqr.helpers.StartUpFragment;
import com.example.seqr.models.ID;
import com.example.seqr.notification.NotificationFragment;
import com.example.seqr.organizer.OrganizerFragment;
import com.example.seqr.profile.DeleteProfileFragment;
import com.example.seqr.profile.EditProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

/**
 * Class representing MainActivity
 */
public class MainActivity extends AppCompatActivity {
    private AttendeeFragment attendeeFragment = new AttendeeFragment();
    private EventLobbyFragment eventLobbyFragment = new EventLobbyFragment();
    private OrganizerFragment organizerFragment = new OrganizerFragment();
    private ImageView profileImageView;
    boolean firstTime = true;
    private Uri bitmapUri;
    private StartUpFragment startUpFragment = new StartUpFragment();
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(MainActivity.this, "Post notification permission granted!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String uuid = ID.getProfileId(getBaseContext());
        System.out.println(uuid);
        if (uuid != null){
            ProfileController profileController = new ProfileController();
            profileController.getProfileUsernameByDeviceId(uuid, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot profileDoc = task.getResult();
                        if (!profileDoc.exists() || profileDoc == null){
                            ID.removeProfileID(getBaseContext());
                        }
                    }else {
                        Log.d("DEBUG","User is still valid in firebase");
                    }
                }
            });
        }



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

            FirebaseApp.initializeApp(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }

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

        // Request to enable permissions on app startup
        String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(permission,1);
        }
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

                // Request to enable permissions when checkbox is marked but permissions are not enabled
                if (isChecked && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(permission,1);
                }
            }
        });
            Button adminButton = findViewById(R.id.admin_button);
            profileImageView = findViewById(R.id.profile_picture);
            String path = Uri.encode("ProfilePictures/" + uuid + ".jpg");
            String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
            Picasso.get().invalidate(imageUrl);
            Picasso.get().load(imageUrl).error(R.drawable.profile_picture_drawer_navigation_icon).into(profileImageView);}

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
                        Bundle bundle = new Bundle();
                        bundle.putString("id", uuid);

                        NotificationFragment notificationFragment = new NotificationFragment();
                        notificationFragment.setArguments(bundle);

                        FragmentTransaction transaction = fragMgr.beginTransaction();
                        transaction.replace(R.id.fragment_container, notificationFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
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
            adminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnterPasswordFragment enterPasswordFragment = new EnterPasswordFragment();
                    enterPasswordFragment.show(fragMgr,"EnterPasswordFragment");
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }
            });

            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("first_time", firstTime); // Pass firstTime to the fragment
                    EditProfileFragment fragment = new EditProfileFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = fragMgr.beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment); // Use the created fragment
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            });

            // make sure this block of code is at the bottom, this is for redirecting to some pages when you click a notification
            if (getIntent().getExtras() != null) {
                Log.d("notfi", "main receieved");
                Boolean ifNotification = getIntent().getExtras().getBoolean("ifNotification");
                String eventID = getIntent().getExtras().getString("eventID");
                Log.d("notfi", "eventID" + eventID);
                if (ifNotification) {
                    String announcementID = getIntent().getExtras().getString("announcementID");
                    Log.d("notfi", "It is a notification. Announcement id" + announcementID);
                    Boolean ifAttendee = true;
                    Bundle bundle = new Bundle();
                    bundle.putString("announcementID", announcementID);
                    bundle.putString("eventID", eventID);
                    bundle.putBoolean("ifAttendee", ifAttendee);
                    AnnouncementDetailFragment announcementDetailFragment = new AnnouncementDetailFragment();
                    announcementDetailFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, announcementDetailFragment)
                            .addToBackStack(null) // Add the fragment transaction to the back stack
                            .commit();
                }
                else {
                    Log.d("notfi", "It is a milestone.");
                    // milestone code
                    Bundle bundle = new Bundle();
                    bundle.putString("eventID", eventID);
                    EventMilestoneFragment milestoneFragment = new EventMilestoneFragment();
                    milestoneFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, milestoneFragment)
                            .addToBackStack(null) // Add the fragment transaction to the back stack
                            .commit();
                }
            }
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

    /**
     * Function to update profile pictures for changes to profile pictures
     * @param imageUri profile picture that was changed
     */
    public void updateProfilePicture(Uri imageUri) {
        Picasso.get().load(imageUri).into(profileImageView);
    }

    public Uri getImageUri(){
        return bitmapUri;
    }

    public void setFirstTime(boolean status) {
        firstTime = status;
    }

    public void setImageUri(Uri imageUri) {
        bitmapUri = imageUri;
    }


    //==============================================================================================
    //End of MainActivity Class
    //
}