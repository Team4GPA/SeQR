package com.example.seqr;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.adapters.QRScanAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Event;
import com.example.seqr.models.ID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing attendee dashboard, including attendee actions such as scanning QR codes and examining the signed up events.
 */
public class AttendeeFragment extends Fragment {
    String qrResult;
    String DBTAG = "AttendeeFragment";
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventsList;

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(DBTAG, "onCreate initialized.");
        View view = inflater.inflate(R.layout.fragment_attendee, container, false);

        recyclerView = view.findViewById(R.id.eventSignedUpRecyclerview);
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventsList, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                Log.d("Debug","Event has been clicked");
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventAdapter);

        String profileUUID = ID.getProfileId(getContext());
        ProfileController profileController = new ProfileController();
        EventController eventController = new EventController();
        profileController.getProfileByUUID(profileUUID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot profileDoc = task.getResult();
                    List<String> signedUpEvents = (List<String>) profileDoc.get("signedUpEvents");
                    if (signedUpEvents != null && !signedUpEvents.isEmpty()){
                        for(String eventID : signedUpEvents){
                            eventController.getEventById(eventID, new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot eventDoc = task.getResult();
                                        if(eventDoc != null && eventDoc.exists()){
                                            Event event = eventDoc.toObject(Event.class);
                                            if (event != null){
                                                eventsList.add(event);
                                                eventAdapter.notifyDataSetChanged();
                                            }
                                        }else {
                                            Log.d("DEBUG","event does not exist in firebase" + eventID);
                                        }

                                    } else{
                                        Log.d("DEBUG","There was some error with getting the event");
                                    }
                                }
                            });
                        }
                    }
                } else{
                    Log.d("DEBUG", "There was some error getting the profile");
                }
            }
        });
        return view;
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view. This gives subclasses a chance
     * to initialize themselves once they know their view hierarchy has been completely created.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(DBTAG, "onViewCreated initialized.");
        super.onViewCreated(view, savedInstanceState);
        ScanQRFragment scanQRFragment = new ScanQRFragment();
        ExtendedFloatingActionButton floatingScanQRButton = view.findViewById(R.id.floatingScanQR);

        //to receive data from the QR Code scanner, set up a ResultListener on "MainActivity" to listen
        //for any "Result" in the app;
        getParentFragmentManager().setFragmentResultListener("reqQR", this.getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.d(DBTAG, "Got to onFragmentResult with bundle data " + bundle.getString("gotQR"));

                qrResult = bundle.getString("gotQR");
                //
                // is result valid?
                //
                if (!qrResult.contentEquals("NULL")){
                    String resultSplit[]= qrResult.split("_"); //this splits off the QR tag on the end of a valid QR tag;
                    String eventID = resultSplit[0];
                    EventController eventController = new EventController();
                    eventController.getAllEvents(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Event event = documentSnapshot.toObject(Event.class);
                                if (event != null) {
                                    // Check if the scanned QR code matches checkInQR or promotionQR of any event
                                    if (eventID.equals(event.getEventID())) {
                                        // QR code is valid
                                        Log.d(DBTAG, "QR Code is valid for event: " + event.getEventName());
                                        launchSuccess(eventID); //fire the transaction for loading the event into fragment container
                                        return;
                                    }
                                }
                            }
                            // If loop completes and no match found, QR code is not valid for any event
                            Log.d(DBTAG, "QR Code is not valid for any event.");
                            launchNotFound();
                        } else {
                            Log.e(DBTAG, "Error fetching events for QR validation", task.getException());

                        }
                    });
                }
                else if (qrResult.contentEquals("NULL")){
                    Log.d(DBTAG, "Scan cancelled or failed.");
                    qrFailed();
                }

            }
        });

        //if the button is clicked, start the QR scanner!
        floatingScanQRButton.setOnClickListener(v -> {
            Log.d(DBTAG, "button clicked; launching scanner fragment");
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, scanQRFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    /**
     * Launches the event info window upon successful initialization on events.
     */
    public void launchSuccess(String QRData) {
        Log.d(DBTAG, "launch success method reached. Firing the event info window: ");
        FragmentManager parent = getParentFragmentManager();
        Fragment eventInfo = new EventInfoFragment();
        Bundle passQR = new Bundle();
        passQR.putString("eventId", QRData);
        eventInfo.setArguments(passQR);

        parent.beginTransaction().replace(R.id.fragment_container, eventInfo).commit();


        parent.beginTransaction().replace(R.id.fragment_container, eventInfo).addToBackStack(null).commit();
    }

    public void qrFailed(){
        Log.d(DBTAG, "qr code scan not successful: return to attendee view");
        Fragment attendee = new AttendeeFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, attendee).commit();

    }

    /**
     * Launches the event info window upon failed initialization.
     */
    public void launchNotFound() {
        Log.d(DBTAG, "qr code scan successful but event not matched: return to attendee view");
        Fragment attendee = new AttendeeFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, attendee).commit();
    }
}

    /*

    A Demonstration "model": AKA, The Data
    public class ListViewModel extends ViewModel {
        private final MutableLiveData<Set<Filter>> filters = new MutableLiveData<>();

        private final LiveData<List<Item>> originalList = ...;
        private final LiveData<List<Item>> filteredList = ...;

        public LiveData<List<Item>> getFilteredList() {
            return filteredList;
        }

        public LiveData<Set<Filter>> getFilters() {
            return filters;
        }

        public void addFilter(Filter filter) { ... }

        public void removeFilter(Filter filter) { ... }
    }

     */

    /* Demo Class: The View, AKA the AttendeeFragment
    public class ListFragment extends Fragment {
        private ListViewModel viewModel;

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            viewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
            viewModel.getFilteredList().observe(getViewLifecycleOwner(), list -> {
                // Update the list UI.
            });
        }
    }

     */

    /* DEMO: The Actor, AKA the ScanQRFragment
    public class FilterFragment extends Fragment {
        private ListViewModel viewModel;

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            viewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
            viewModel.getFilters().observe(getViewLifecycleOwner(), set -> {
                // Update the selected filters UI.
            });
        }

        public void onFilterSelected(Filter filter) {
            viewModel.addFilter(filter);
        }

        public void onFilterDeselected(Filter filter) {
            viewModel.removeFilter(filter);
        }
    }

     */

    /*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
        @Override
        public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
            // We use a String here, but any type that can be put in a Bundle is supported.
            String result = bundle.getString("bundleKey");
            // Do something with the result.
        }
    });
}
     */

    //To receive events, we watch the QRScanAdapter.
    //QRScanAdapter scanAdapter = new ViewModelProvider(requireActivity()).get(QRScanAdapter.class);
    //scanAdapter.getQRCodeResult().observe(getViewLifecycleOwner(), list -> {
    // Update the list UI.
    //});