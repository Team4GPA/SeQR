package com.example.seqr.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.seqr.R;
import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.database.Database;
import com.example.seqr.models.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private String id;
    private String eventName;

    private FirebaseFirestore db;

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
        return view;
    }

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
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15.0f));
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle){
        super.onViewCreated(view, bundle);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        if (mapFragment != null){
            mapFragment.getMapAsync(this);
        }
    }
}
