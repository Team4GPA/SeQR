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

/**
 * Fragment to select the location of an event
 */
public class CEventLocationFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap map;
    private Button backButton;
    private Button nextButton;

    private LatLng latLng;

    /**
     * Method to create fragment and handle any calculations done inside
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the associated view of the fragment
     */
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

    /**
     * Method to edit the map once it is loaded
     * @param googleMap a map to load into the fragment
     */
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
