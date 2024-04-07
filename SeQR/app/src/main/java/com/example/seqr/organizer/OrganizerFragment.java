package com.example.seqr.organizer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seqr.R;
import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.events.CEventDetailFragment;
import com.example.seqr.events.EventManagementFragment;
import com.example.seqr.models.Event;
import com.example.seqr.models.ID;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing organizer dashboard, including organizer actions such as creating events and examining the past created events.
 */
public class OrganizerFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventsList;

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
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_organizer, container, false);
        ExtendedFloatingActionButton createEventButton = view.findViewById(R.id.floatingCreateEvent);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CEventDetailFragment cEventDetailFragment = new CEventDetailFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, cEventDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        recyclerView = view.findViewById(R.id.eventCreatedRecyclerview);
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventsList, this::onItemClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventAdapter);
        String organizerUUID = ID.getProfileId(getContext());
        // get the events
        EventController eventController = new EventController();
        eventController.getEventsByOrganizer(organizerUUID, task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    eventsList.add(event);
                }
                Collections.sort(eventsList, (a1, a2) -> a2.getCreatedTime().compareTo(a1.getCreatedTime()));
                eventAdapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG", "there was some error in event retrieval", task.getException());
            }
        });








        return view;
    }

    /**
     * Adds functionality to click each item on recycler view
     * @param event event to be viewed by user
     */
    public void onItemClick(Event event) {

        Bundle bundle = new Bundle();
        bundle.putString("eventID",event.getEventID());
        bundle.putString("checkInQR",event.getCheckInQR());
        bundle.putString("promotionQR",event.getPromotionQR());
        bundle.putString("eventName",event.getEventName());
        bundle.putString("organizer", event.getOrganizer());

        EventManagementFragment eventManagementFragment = new EventManagementFragment();
        eventManagementFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, eventManagementFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }










}