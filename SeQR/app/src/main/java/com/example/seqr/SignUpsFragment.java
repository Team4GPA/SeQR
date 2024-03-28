package com.example.seqr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seqr.adapters.ProfileAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SignUpsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_ups, container, false);

        recyclerView = view.findViewById(R.id.signUps_profiles);
        profileList = new ArrayList<>();

        Bundle bundle = getArguments();
        assert bundle != null;

        EventController eventController = new EventController();
        String eventID = bundle.getString("eventID","");
        eventController.getEventSignUps(eventID, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });

        return view;
    }
}