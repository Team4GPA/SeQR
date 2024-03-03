package com.example.seqr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}