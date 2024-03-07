package com.example.seqr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.example.seqr.controllers.EventController;
import com.example.seqr.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.TextView;
import com.example.seqr.models.ID;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Profile;

public class MainActivity extends AppCompatActivity {

    private AttendeeFragment attendeeFragment = new AttendeeFragment();
    private EventLobbyFragment eventLobbyFragment = new EventLobbyFragment();
    private OrganizerFragment organizerFragment = new OrganizerFragment();
    QRCodeGenerator test;
    //Bitmap map;
    //Button gen;
    //ImageView dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //gen = findViewById(R.id.tester);
        //dis = findViewById(R.id.testdisplay);
      
        /*gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "E0001";
                //map = test.generate(id);
                BitMatrix mMatrix;
                MultiFormatWriter mWriter = new MultiFormatWriter();
                BarcodeEncoder mEncoder = new BarcodeEncoder();
                try{
                    mMatrix = mWriter.encode(id, BarcodeFormat.QR_CODE, 400, 400);
                    map = mEncoder.createBitmap(mMatrix);
                    dis.setImageBitmap(map);
                    //displayQR(map);
                }
                catch(WriterException e){
                    e.printStackTrace();
                }
                //displayQR(map);
            }
        });*/

        String uuid = ID.getProfileId(this);

        //if device hasn't opened the app before and made a username need to add extra checks to make sure they actually created
        if (uuid == null) {
            startUpLogic();
        } else {
            setContentView(R.layout.activity_main);}



        // initialize buttons for the side menu
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        CheckBox enableGeoLocationCheckbox = findViewById(R.id.enable_geo_location_checkbox);
        Button adminButton = findViewById(R.id.admin_button);

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

//        //Button to skip to Upload event poster fragment and test it
//        Button tester = findViewById(R.id.testImageUpFrag);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
//            public void onClick(View v) {
//                FragmentTransaction swapMain = fragMgr.beginTransaction();
//                Fragment cameraFrag = new ScanQRFragment();
//                swapMain.replace(mainFrag.getId(), cameraFrag);
//                swapMain.commit();
//            }
//        });


//        tester.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              FragmentTransaction swapTo = fragMgr.beginTransaction();
//              Fragment posterUpFrag = new UploadPosterFragment();
//              Fragment tester = new AdminFragment();
//              swapTo.replace(R.id.fragment_container, posterUpFrag);
//              swapTo.commit();
//          }
//      });


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
    }

//  private void openFragment(Fragment fragment) {
//        FragmentTransaction transaction = fragMgr.beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//  }

    private void testAddProfile(){
        ProfileController profileController = new ProfileController();
        Profile newProfile = new Profile("Testing Adding Again", "Placeholder for UUID");
        profileController.addProfile(newProfile);
    }

    private void startUpLogic(){
        setContentView(R.layout.start_up);
        EditText userNameEntry = findViewById(R.id.enteredUsername);
        Button confirmButton = findViewById(R.id.createProfileConfirmButton);

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