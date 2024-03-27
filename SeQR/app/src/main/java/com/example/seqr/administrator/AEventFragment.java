package com.example.seqr.administrator;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.Event;
import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.controllers.EventController;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment showing all events for administrator.
 */

public class AEventFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    /**
     * Creates a view and associated logic for the view and returns it to whoever built the fragment
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
        View view = inflater.inflate(R.layout.fragment_a_events, container, false);

        // Create back the back button
        Button backButton = view.findViewById(R.id.admin_events_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        FragmentManager fragMgr = getFragmentManager();
        // Create recycler view
        recyclerView = view.findViewById(R.id.admin_events);
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event){
                Bundle bundle = new Bundle();
                bundle.putString("eventName",event.getEventName());
                bundle.putString("eventDesc", event.getEventDesc());
                bundle.putString("maxCapacity", Integer.toString(event.getMaxCapacity()));
                bundle.putString("organizer", event.getOrganizer());
                bundle.putString("location", event.getLocation());
                bundle.putString("eventStartTime", event.getEventStartTime());
                bundle.putString("eventID", event.getEventID());


                AEditEventFragment editEventFragment = new AEditEventFragment();
                editEventFragment.setArguments(bundle);
                fragMgr.beginTransaction().replace(R.id.fragment_container, editEventFragment).addToBackStack(null).commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(eventAdapter);

        // Query to get all profiles
        EventController eventController = new EventController();
        eventController.getAllEvents(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG", "there was some error event in Event retrieval", task.getException());
            }
        });

        return view;
    }
}
