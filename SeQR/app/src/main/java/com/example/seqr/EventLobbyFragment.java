package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.models.Event;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment for displaying a list of possible events for users in the lobby.
 */
public class EventLobbyFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_event_lobby, container, false);

        recyclerView = view.findViewById(R.id.eventLobbyRecyclerview);
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventsList, this::eventClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventAdapter);

        // get the events to event lobby
        EventController eventController = new EventController();
        eventController.getAllEvents(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    eventsList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG", "there was some error in event retrieval to event lobby", task.getException());
            }
        });
        return view;
    }

    /**
     * Send user to event page of the event clicked
     * @param event event that was clicked by the user
     */
    private void eventClicked(Event event){
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getEventID());

        EventInfoFragment eventInfoFragment = new EventInfoFragment();
        eventInfoFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventInfoFragment)
                .addToBackStack(null)
                .commit();


    }
}