package com.example.seqr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.seqr.announcements.AnnouncementDetailFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent().getExtras() != null) {
            // from notification
            Log.d("notfi", "Splash receieved");
            String eventID = getIntent().getExtras().getString("eventID");
            Boolean ifNotification = getIntent().getExtras().getBoolean("ifNotification");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("eventID", eventID);
            if (ifNotification) {
                String announcementID = getIntent().getExtras().getString("announcementID");
                intent.putExtra("announcementID", announcementID);
                intent.putExtra("ifNotification", true);
            }
            else {
                intent.putExtra("ifNotification", false);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}