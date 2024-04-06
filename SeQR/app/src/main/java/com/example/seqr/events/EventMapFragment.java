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
import com.example.seqr.models.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap eMap){
        map = eMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng edmonton = new LatLng(53.5461,-113.4937);
        map.addMarker(new MarkerOptions().position(edmonton).title("Edmonton"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(edmonton));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(edmonton, (float) 18.0));
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
