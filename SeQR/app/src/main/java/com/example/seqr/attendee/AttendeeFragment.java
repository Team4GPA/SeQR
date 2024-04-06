package com.example.seqr.attendee;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seqr.events.EventInfoFragment;
import com.example.seqr.R;
import com.example.seqr.qr.ScanQRFragment;
import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Event;
import com.example.seqr.models.ID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A fragment representing attendee dashboard, including attendee actions such as scanning QR codes and examining the signed up events.
 */
public class AttendeeFragment extends Fragment {
    String qrResult;
    String DBTAG = "AttendeeFragment";
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventsList;
    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Give a second for profile controller to update and display all events you're attending
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Log.d(DBTAG, "onCreate initialized.");
        View view = inflater.inflate(R.layout.fragment_attendee, container, false);

        recyclerView = view.findViewById(R.id.eventSignedUpRecyclerview);
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventsList, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                Log.d("Debug","Event has been clicked");
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventAdapter);

        String profileUUID = ID.getProfileId(getContext());
        ProfileController profileController = new ProfileController();
        EventController eventController = new EventController();
        profileController.getProfileByUUID(profileUUID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot profileDoc = task.getResult();
                    List<String> signedUpEvents = (List<String>) profileDoc.get("signedUpEvents");
                    if (signedUpEvents != null && !signedUpEvents.isEmpty()){
                        for(String eventID : signedUpEvents){
                            eventController.getEventById(eventID, new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task_two) {
                                    if(task_two.isSuccessful()){
                                        DocumentSnapshot eventDoc = task_two.getResult();
                                        if(eventDoc != null && eventDoc.exists()){
                                            Event event = eventDoc.toObject(Event.class);
                                            if (event != null){
                                                eventsList.add(event);
                                                eventAdapter.notifyDataSetChanged();
                                                recyclerView.setAdapter(eventAdapter);
                                            }
                                        }else {
                                            Log.d("DEBUG","event does not exist in firebase" + eventID);
                                        }

                                    } else{
                                        Log.d("DEBUG","There was some error with getting the event");
                                    }
                                }
                            });
                        }
                    }
                } else{
                    Log.d("DEBUG", "There was some error getting the profile");
                }
            }
        });
        return view;
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view. This gives subclasses a chance
     * to initialize themselves once they know their view hierarchy has been completely created.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(DBTAG, "onViewCreated initialized.");
        super.onViewCreated(view, savedInstanceState);
        // retrieveSignedUpEvents();
        ScanQRFragment scanQRFragment = new ScanQRFragment();
        ExtendedFloatingActionButton floatingScanQRButton = view.findViewById(R.id.floatingScanQR);

        //to receive data from the QR Code scanner, set up a ResultListener on "MainActivity" to listen
        //for any "Result" in the app;
        getParentFragmentManager().setFragmentResultListener("reqQR", this.getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.d(DBTAG, "Got to onFragmentResult with bundle data " + bundle.getString("gotQR"));

                qrResult = bundle.getString("gotQR");
                //
                // is result valid?
                //
                if (!qrResult.contentEquals("NULL")){
                    String resultSplit[]= qrResult.split("_"); //this splits off the QR tag on the end of a valid QR tag;
                    String eventID = resultSplit[0];
                    String qrType = resultSplit[1];


                    EventController eventController = new EventController();
                    eventController.getAllEvents(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Event event = documentSnapshot.toObject(Event.class);
                                if (event != null) {
                                    // Check if the scanned QR code matches checkInQR or promotionQR of any event
                                    if (eventID.equals(event.getEventID())) {
                                        // QR code is valid
                                        Log.d(DBTAG, "QR Code is valid for event: " + event.getEventName());
                                        if (qrType.equals("promotion")){
                                            launchSuccess(eventID); //fire the transaction for loading the event into fragment container
                                            return;
                                        }
                                        else if (qrType.equals("checkIn")){
                                            Log.d("DEBUG","this was a checkIN QR");
                                            launchCheckInSuccess(eventID);
                                            return;
                                        }

                                    }
                                }
                            }
                            // If loop completes and no match found, QR code is not valid for any event
                            Log.d(DBTAG, "QR Code is not valid for any event.");
                            launchNotFound();
                        } else {
                            Log.e(DBTAG, "Error fetching events for QR validation", task.getException());

                        }
                    });
                }
                else if (qrResult.contentEquals("NULL")){
                    Log.d(DBTAG, "Scan cancelled or failed.");
                    qrFailed();
                }

            }
        });


        //if the button is clicked, start the QR scanner!
        floatingScanQRButton.setOnClickListener(v -> {
            Log.d(DBTAG, "button clicked; launching scanner fragment");
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, scanQRFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    public void retrieveSignedUpEvents(){
        String profileUUID = ID.getProfileId(getContext());
        ProfileController profileController = new ProfileController();
        EventController eventController = new EventController();

        eventsList.clear(); // remove the existing events so no duplicates
        eventAdapter.notifyDataSetChanged();
        profileController.getProfileByUUID(profileUUID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot profileDoc = task.getResult();
                    List<String> signedUpEvents = (List<String>) profileDoc.get("signedUpEvents");
                    if (signedUpEvents != null && !signedUpEvents.isEmpty()){
                        for(String eventID : signedUpEvents){
                            eventController.getEventById(eventID, new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task_two) {
                                    if(task_two.isSuccessful()){
                                        DocumentSnapshot eventDoc = task_two.getResult();
                                        if(eventDoc != null && eventDoc.exists()){
                                            Event event = eventDoc.toObject(Event.class);
                                            if (event != null){
                                                eventsList.add(event);
                                                eventAdapter.notifyDataSetChanged();
                                            }
                                        }else {
                                            Log.d("DEBUG","event does not exist in firebase" + eventID);
                                        }

                                    } else{
                                        Log.d("DEBUG","There was some error with getting the event");
                                    }
                                }
                            });
                        }
                    }
                } else{
                    Log.d("DEBUG", "There was some error getting the profile");
                }
            }
        });
    }

    /**
     * Launches the event info window upon successful initialization on events.
     */
    public void launchSuccess(String QRData) {
        Log.d(DBTAG, "launch success method reached. Firing the event info window: ");
        FragmentManager parent = getParentFragmentManager();
        Fragment eventInfo = new EventInfoFragment();
        Bundle passQR = new Bundle();
        passQR.putString("eventID", QRData);
        eventInfo.setArguments(passQR);
        parent.beginTransaction().replace(R.id.fragment_container, eventInfo).addToBackStack(null).commit();
    }

    public void launchCheckInSuccess(String QRData){
        Log.d(DBTAG, "launch success method reached. Firing the event info window: ");

        EventController eventController = new EventController();
        String userID = ID.getProfileId(getContext());
        ProfileController profileController = new ProfileController();

        // Get the profile to get its permissions and device location
        String profileUUID = ID.getProfileId(getContext());
        AtomicBoolean geolocation = new AtomicBoolean(false);
        profileController.getProfileByUUID(profileUUID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot profileDoc = task.getResult();
                    Boolean geo = (Boolean) profileDoc.get("geoLocation");
                    geolocation.set(geo);
                }
            }
        });
        double[] latLng = {};
        if (geolocation.get() && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCoordinates(LocationManager.GPS_PROVIDER, new LocationCallback() {
                @Override
                public void onLocationReceived(double latitude, double longitude) {
                    // Do something with the location data
                    eventController.updateEventCheckInCoordinates(QRData,profileUUID, latitude,longitude);
                }
            });
        } else if (geolocation.get() && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCoordinates(LocationManager.GPS_PROVIDER, new LocationCallback() {
                @Override
                public void onLocationReceived(double latitude, double longitude) {
                    eventController.updateEventCheckInCoordinates(QRData,profileUUID, latitude,longitude);
                }
            });
        }
        profileController.getProfileUsernameByDeviceId(userID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String username = task.getResult().getString("username");
                    if (username!= null) {
                        eventController.checkInUser(QRData, userID, username, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("DEBUG", "successfully checked in the user");
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("DEBUG","there was an issue with checking In");
                            }
                        });
                    }
                }
            }
        });
        FragmentManager parent = getParentFragmentManager();
        Fragment eventInfo = new EventInfoFragment();
        Bundle passQR = new Bundle();
        passQR.putString("eventID", QRData);
        eventInfo.setArguments(passQR);
        parent.beginTransaction().replace(R.id.fragment_container, eventInfo).addToBackStack(null).commit();
    }

    public void qrFailed(){
        Log.d(DBTAG, "qr code scan not successful: return to attendee view");
        Fragment attendee = new AttendeeFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, attendee).commit();

    }

    /**
     * Launches the event info window upon failed initialization.
     */
    public void launchNotFound() {
        Log.d(DBTAG, "qr code scan successful but event not matched: return to attendee view");
        Fragment attendee = new AttendeeFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, attendee).commit();
    }


    public void getCoordinates(String provider, LocationCallback callback) {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(provider)) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, handle accordingly
                return;
            }
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    callback.onLocationReceived(latitude,longitude);
                }

                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            }, null);

            }
        }

    }
