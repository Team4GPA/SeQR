package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A fragment for adding event details.
 */
public class CEventDetailFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_detail, container, false);
        Button backButton = view.findViewById(R.id.BackButton);
        Button nextButton = view.findViewById(R.id.cEventDetailNextButton);
        EditText eventNameEnter = view.findViewById(R.id.eventNameInput);
        EditText eventTimeInput = view.findViewById(R.id.eventTimeInput);
        EditText eventLocationInput = view.findViewById(R.id.eventLocationInput);
        EditText eventDescriptionInput = view.findViewById(R.id.eventDescriptionInput);
        EditText capacityInput = view.findViewById(R.id.eventCapacityInput);
      
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName =  eventNameEnter.getText().toString();
                String eventTime = eventTimeInput.getText().toString();
                String eventLocation = eventLocationInput.getText().toString();
                String eventDescription = eventDescriptionInput.getText().toString();
                String eventCapacityInput = capacityInput.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventTime", eventTime);
                bundle.putString("eventLocation", eventLocation);
                bundle.putString("eventDescription",eventDescription);
                bundle.putString("eventCapacity", eventCapacityInput);

                CEventImageFragment cEventImageFragment = new CEventImageFragment();
                cEventImageFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, cEventImageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}