package com.example.seqr;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.ID;
import com.example.seqr.models.Profile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uuid = ID.getProfileId(getBaseContext());

        if (uuid == null){
            startUpLogic();
        }
        else{
            setContentView(R.layout.activity_main);
        }
    }

    private void startUpLogic(){
        setContentView(R.layout.start_up);
        EditText userNameEntry = findViewById(R.id.enteredUsername);
        Button confirmButton = findViewById(R.id.createProfileConfirmButton);
        Context thisContext = this;

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","Onclick reached");
                // call profile controller we are about to add a profile
                ProfileController profileController = new ProfileController();
                String username =  userNameEntry.getText().toString();
                String uuid = ID.createProfileID(thisContext);
                Profile newProfile = new Profile(username, uuid);
                profileController.addProfile(newProfile);
                setContentView(R.layout.activity_main);
            }
        });
    }
}
