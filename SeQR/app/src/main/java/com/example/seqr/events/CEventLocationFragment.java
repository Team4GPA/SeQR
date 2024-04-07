package com.example.seqr.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.seqr.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.annotation.Nullable;

public class CEventLocationFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap map;
    private Button backButton;
    private Button nextButton;

    private LatLng latLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_event_location, container, false);

        backButton = view.findViewById(R.id.back_button);
        nextButton = view.findViewById(R.id.next_button);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLng == null) {
                    Toast.makeText(getContext(), "Please select a location on the map",Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = getArguments();
                    assert bundle != null;
                    bundle.putDouble("latitude",latLng.latitude);
                    bundle.putDouble("longitude",latLng.longitude);
                    CEventCQRFragment cEventCQRFragment = new CEventCQRFragment();
                    cEventCQRFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, cEventCQRFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng coordinates){
                map.clear();
                latLng = coordinates;
                map.addMarker(new MarkerOptions().position(latLng));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));
            }

        });
    }


}
