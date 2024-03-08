package com.example.seqr.models;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * A model class for managing unique device IDs associated with the application.
 */
//this class checks if user has opened the app before and has an ID
public class ID {
    private static final String prefs = "AppPreferences";
    private static final String deviceUUID = "id";

    // link for more info about shared prefs https://developer.android.com/training/data-storage/shared-preferences#java
    //returns deviceID from sharedPrefs

    /**
     * Retrieves the unique profile (device) ID associated with the device from SharedPreferences.
     *
     * @param context The application context.
     * @return The profile (device) ID if it exists, otherwise null.
     */
    public static String getProfileId(Context context){
        //SharedPref lets us store data on device
        SharedPreferences preferences = context.getSharedPreferences(prefs, context.MODE_PRIVATE);
        //checks if we already stored an id in shared pref returns null if nothing
        String deviceId = preferences.getString(deviceUUID,null);

        return deviceId;
    }

    /**
     * Generates and retrieves a unique profile ID associated with the device. If a profile ID does not already exist, a new one is generated and stored in SharedPreferences.
     *
     * @param context The application context.
     * @return The profile ID.
     */
    //returns deviceID
    public static String createProfileID(Context context){
        //SharedPref lets us store data on device
        SharedPreferences preferences = context.getSharedPreferences(prefs, context.MODE_PRIVATE);
        //checks if we already stored an id in shared pref returns null if nothing
        String deviceId = preferences.getString(deviceUUID,null);

        //if there is no stored ID
        if(deviceId == null){
            deviceId = UUID.randomUUID().toString();

            // put new id in sharedPreferences
            preferences.edit().putString(deviceUUID, deviceId).apply();
        }

        return deviceId;

    }

    /**
     * Removes the unique profile ID associated with the device from SharedPreferences.
     *
     * @param context The application context.
     */
    //functionality to remove the uuid from shared pref
    public static void removeProfileID(Context context){
        SharedPreferences preferences = context.getSharedPreferences(prefs, context.MODE_PRIVATE);
        //removes uuid from the shared preferences
        preferences.edit().remove(deviceUUID).apply();


    }
}
