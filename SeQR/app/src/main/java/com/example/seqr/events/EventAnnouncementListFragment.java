package com.example.seqr.events;

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

import com.example.seqr.announcements.AnnouncementDetailFragment;
import com.example.seqr.announcements.CAnnouncementFragment;
import com.example.seqr.R;
import com.example.seqr.adapters.AnnouncementAdapter;
import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.models.Announcement;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment to show all the announcements associated with an event
 */
public class EventAnnouncementListFragment extends Fragment {
    private RecyclerView recyclerView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcementList;
    private String eventID;

    /**
     * A method to handle creating the view associated with the fragment and any calculations made by it
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the view associated with the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event_announcement_list, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        eventID = bundle.getString("eventID","");
        Boolean ifOrganizer = bundle.getBoolean("ifOrganizer", false);

        recyclerView = view.findViewById(R.id.EALRecyclerview);
        announcementList = new ArrayList<>();
        announcementAdapter = new AnnouncementAdapter(announcementList, (announcement) -> announcementClicked(announcement, ifOrganizer));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(announcementAdapter);
        Button EALBackButton = view.findViewById(R.id.EALBackButton);
        Button EALCreateButton = view.findViewById(R.id.EALCreateButton);

        if (ifOrganizer) {
            EALCreateButton.setVisibility(View.VISIBLE);
            EALCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CAnnouncementFragment cAnnouncementFragment = new CAnnouncementFragment();
                    cAnnouncementFragment.setArguments(bundle);

                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, cAnnouncementFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
        else {
            EALCreateButton.setVisibility(View.GONE);
        }

        AnnouncementController announcementController = new AnnouncementController();
        announcementController.getAnnouncementsByEvent(eventID, task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Announcement announcement = document.toObject(Announcement.class);
                    announcementList.add(announcement);
                }
                Collections.sort(announcementList, (a1, a2) -> a2.getTime().compareTo(a1.getTime()));
                announcementAdapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG", "there was some error in announcement retrieval", task.getException());
            }
        });

        EALBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        return view;
    }

    /**
     * A method to handle when an announcement is clicked
     * @param announcement the announcement
     * @param ifOrganizer a boolean which is true if the user is an organizer
     */
    public void announcementClicked (Announcement announcement, Boolean ifOrganizer) {
        Bundle bundle = new Bundle();
        bundle.putString("announcementID", announcement.getAnnouncementID());
        bundle.putString("eventID", eventID);
        bundle.putBoolean("ifAttendee", !ifOrganizer);
        //open announcement field
        AnnouncementDetailFragment announcementDetailFragment = new AnnouncementDetailFragment();
        announcementDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, announcementDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}