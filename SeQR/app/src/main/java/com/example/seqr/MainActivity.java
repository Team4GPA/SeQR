package com.example.seqr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seqr.models.ID;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Profile;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uuid = ID.getProfileId(this);
        //if device hasn't opened the app before and made a username need to add extra checks to make sure they actually created
        if (uuid == null) {

            startUpLogic();
        } else {
            setContentView(R.layout.activity_main);









        //for testing: add a floating QR button over the main fragment view 'fragment_container'
        ExtendedFloatingActionButton qrButton = findViewById(R.id.scanQRButton);

        //setup the main fragment view stuff
        FragmentManager fragMgr = getSupportFragmentManager();
        View mainFrag = findViewById(R.id.fragment_container);

        //sliding drawer layout on left-hand side of Activity window
        DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout);

        // Handle on clicks for bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_attendee) {
                    // Handle attendee button
                    return true;
                } else if (id == R.id.bottom_organizer) {
                    // Handle organizer button
                    return true;
                } else if (id == R.id.bottom_events) {
                    // Handle events button
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle navigation icon click
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //handle clicks on the QR code button;
        //uses some variables set up earlier:
        //mainFrag: a view in the XML for the main_activity
        //fragMgr: Android manager of fragments
        //
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction swapMain = fragMgr.beginTransaction();
                Fragment cameraFrag = new ScanQRFragment();
                swapMain.replace(mainFrag.getId(), cameraFrag);
                swapMain.commit();
            }
        });
    }
    }

    private void testAddProfile(){
        ProfileController profileController = new ProfileController();
        Profile newProfile = new Profile("Testing Adding Again", "Placeholder for UUID");
        profileController.addProfile(newProfile);

    }

    private void startUpLogic(){
        setContentView(R.layout.start_up);
        EditText userNameEntry = findViewById(R.id.enteredUsername);
        Button confirmButton = findViewById(R.id.signUpConfirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","Onclick reached");
                // call profile controller we are about to add a profile
                ProfileController profileController = new ProfileController();
                String username =  userNameEntry.getText().toString();
                String uuid = ID.createProfileID(MainActivity.this);
                Profile newProfile = new Profile(username, uuid);
                profileController.addProfile(newProfile);
                setContentView(R.layout.activity_main);

            }
        });
    }

}