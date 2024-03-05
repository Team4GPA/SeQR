package com.example.seqr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for testing: add a floating QR button over the main fragment view 'fragment_container'
        ExtendedFloatingActionButton qrButton = findViewById(R.id.scanQRButton);
        String qrResult;

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
                if (id == R.id.bottom_attendee){
                    // Handle attendee button
                    return true;
                } else if (id == R.id.bottom_organizer){
                    // Handle organizer button
                    return true;
                } else if (id == R.id.bottom_events){
                    // Handle events button
                    return true;
                }

                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item){
                if (item.getItemId() == R.id.notification_icon){
                    // Handle notifcation icon click
                    return true;
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Handle navigation icon click
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //handle clicks on the QR code button;
        //uses some variables set up earlier:
        //mainFrag: a view in the XML for the main_activity (acts as a container for fragments)
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