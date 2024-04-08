package com.example.seqr.events;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.seqr.R;
import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.database.Database;
import com.example.seqr.models.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment to show where the event is and where attendees are checking in from
 */
public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private String id;
    private String eventName;

    private FirebaseFirestore db;
    private Button backButton;

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Bundle bundle = getArguments();
        if (bundle != null){
            id = bundle.getString("eventID");
            eventName = bundle.getString("eventName");
        }

        db = Database.getFireStore();
        backButton = view.findViewById(R.id.map_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    /**
     * Fills in and edits the map once it is loaded into the fragment
     * @param eMap the googleMap to load into the fragment
     */
    @Override
    public void onMapReady(GoogleMap eMap){
        map = eMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        db.collection("Events").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    double latitude = documentSnapshot.getDouble("latitude");
                    double longitude = documentSnapshot.getDouble("longitude");

                    LatLng eventLocation = new LatLng(latitude,longitude);
                    map.addMarker(new MarkerOptions().position(eventLocation).title(eventName));
                    pinCheckins();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 7.0f));
                } else {
                    Log.d("DEBUG", "DOCUMENT DOES NOT EXIST");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG", "Couldn't retrieve event to locate on map", e);
            }
        });
    }

    /**
     * Method to handle when the view is created which simply creates a maap
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param bundle If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle){
        super.onViewCreated(view, bundle);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        if (mapFragment != null){
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Method to get all of the users who are checked in and set them as pins on the map
     */
    public void pinCheckins(){
        EventController eControl = new EventController();
        ProfileController pControl = new ProfileController();

        eControl.getEventCheckIns(id, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(!querySnapshot.isEmpty()){
                        for (DocumentSnapshot checkInDoc: task.getResult()){
                            if (checkInDoc.contains("latitude") && checkInDoc.contains("longitude")){
                                String username = checkInDoc.getString("username");
                                String profileID = checkInDoc.getId();
                                double latitude = checkInDoc.getDouble("latitude");
                                double longitude = checkInDoc.getDouble("longitude");

                                pControl.getProfileByUUID(profileID, new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot profileDoc = task.getResult();
                                            Boolean locationFlag = profileDoc.getBoolean("geoLocation");

                                            if (locationFlag){
                                                LatLng checkInPin = new LatLng(latitude,longitude);
                                                map.addMarker(new MarkerOptions().position(checkInPin)
                                                        .title(username)
                                                        .icon(getMarkerIcon("#069AF3")));
                                            }
                                        }else{
                                            Log.d("DEBUG", "error in retrieving profile");
                                        }
                                    }
                                });
                            }
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "No CheckIns yet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("DEBUG","error in getting checkIns");
                }
            }
        });
    }

    /**
     * Method to get a BitmapDescriptor for a certain color to set a pin color to
     * @param color the color to seet the pin
     * @return a BitmapDescriptor with the color "color"
     */
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
