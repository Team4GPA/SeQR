package com.example.seqr;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // Create recycler view
        recyclerView = view.findViewById(R.id.admin_events);
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);

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
