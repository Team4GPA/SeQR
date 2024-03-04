package com.example.seqr.models;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

//this class checks if user has opened the app before and has an ID
public class ID {
    private static final String prefs = "AppPreferences";
    private static final String deviceUUID = "id";

    public static String getProfileId(Context context){
        //SharedPref lets us store data on device
        SharedPreferences preferences = context.getSharedPreferences(prefs, context.MODE_PRIVATE);
        //checks if we already stored an id in shared pref
        String deviceId = preferences.getString(deviceUUID,null);

        //if there is no stored ID
        if(deviceId == null){
            deviceId = UUID.randomUUID().toString();

            // put new id in sharedPreferences
            preferences.edit().putString(deviceUUID, deviceId).apply();
        }

        return deviceId;
    }
}
