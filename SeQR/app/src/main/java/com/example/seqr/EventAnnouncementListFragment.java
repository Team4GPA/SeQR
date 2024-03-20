package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seqr.adapters.AnnouncementAdapter;
import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.models.Announcement;
import com.example.seqr.models.Event;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventAnnouncementListFragment extends Fragment {
    private RecyclerView recyclerView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcementList;
    private String eventID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event_announcement_list, container, false);
        recyclerView = view.findViewById(R.id.EALRecyclerview);
        announcementList = new ArrayList<>();
        announcementAdapter = new AnnouncementAdapter(announcementList, this::announcementClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(announcementAdapter);
        Button EALBackButton = view.findViewById(R.id.EALBackButton);
        EALBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        Bundle bundle = getArguments();
        assert bundle != null;
        eventID = bundle.getString("eventID","");

        AnnouncementController announcementController = new AnnouncementController();
        announcementController.getAnnouncementsByEvent(eventID, task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Announcement announcement = document.toObject(Announcement.class);
                    announcementList.add(announcement);
                }
                announcementAdapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG", "there was some error in announcement retrieval", task.getException());
            }
        });

        return view;
    }

    public void announcementClicked (Announcement announcement) {
        Bundle bundle = new Bundle();
        bundle.putString("announcementID", announcement.getAnnouncementID());
        //open announcement field
        AnnouncementDetailFragment announcementDetailFragment = new AnnouncementDetailFragment();
        announcementDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, announcementDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}