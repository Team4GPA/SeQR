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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.seqr.adapters.QRScanAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.models.Event;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

public class AttendeeFragment extends Fragment {
    String qrResult;
    String DBTAG = "AttendeeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(DBTAG, "onCreate initialized.");
        View view = inflater.inflate(R.layout.fragment_attendee, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(DBTAG, "onViewCreated initialized.");
        super.onViewCreated(view, savedInstanceState);
        ScanQRFragment scanQRFragment = new ScanQRFragment();
        ExtendedFloatingActionButton floatingScanQRButton = view.findViewById(R.id.floatingScanQR);

        getParentFragmentManager().setFragmentResultListener("reqQR", this.getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.d(DBTAG, "Got to onFragmentResult with bundle data " + bundle.getString("gotQR"));

                qrResult = bundle.getString("gotQR");
                //
                // is result valid?
                //
                String resultSplit[]= qrResult.split("_");
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
                                    launchSuccess();
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
        });

        floatingScanQRButton.setOnClickListener(v -> {
            Log.d(DBTAG, "button clicked; launching scanner fragment");
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, scanQRFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    public void launchSuccess() {
        Log.d(DBTAG, "launch success method reached.");
        FragmentManager parent = getParentFragmentManager();
        Fragment eventInfo = new A_Test_Fragment();
        parent.beginTransaction().replace(R.id.fragment_container, eventInfo).commit();
    }

    public void launchNotFound() {

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