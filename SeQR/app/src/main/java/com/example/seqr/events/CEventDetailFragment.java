package com.example.seqr.events;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seqr.R;

/**
 * A fragment for adding event details.
 */
public class CEventDetailFragment extends Fragment {
    boolean limitCapacity = false;
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
        Button backButton = view.findViewById(R.id.QRCheckInBackButton);
        Button nextButton = view.findViewById(R.id.cEventDetailNextButton);
        EditText eventNameEnter = view.findViewById(R.id.eventNameInput);
        EditText eventTimeInput = view.findViewById(R.id.eventTimeInput);
        EditText eventLocationInput = view.findViewById(R.id.eventLocationInput);
        EditText eventDescriptionInput = view.findViewById(R.id.eventDescriptionInput);
        EditText capacityInput = view.findViewById(R.id.eventCapacityInput);
        CheckBox hasCapacity = view.findViewById(R.id.hasCapacity);

      
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        hasCapacity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                limitCapacity = true;
            }
            else{
                limitCapacity = false;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName =  eventNameEnter.getText().toString();
                String eventTime = eventTimeInput.getText().toString();
                String eventLocation = eventLocationInput.getText().toString();
                String eventDescription = eventDescriptionInput.getText().toString();
                String eventCapacityInput = "-1";
                if (limitCapacity){
                    eventCapacityInput = capacityInput.getText().toString();
                }

                View[] entryArray = {
                        eventNameEnter,
                        eventTimeInput,
                        eventLocationInput,
                        eventDescriptionInput,
                        capacityInput,
                };

                //double-check entries are valid before creating the event;
                for (View entry : entryArray){
                    checkField(entry);
                }

                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventTime", eventTime);
                bundle.putString("eventLocation", eventLocation);
                bundle.putString("eventDescription",eventDescription);
                if (limitCapacity){
                    bundle.putString("eventCapacity", eventCapacityInput);
                }
                else{
                    bundle.putString("eventCapacity", "-1");
                }

                CEventImageFragment cEventImageFragment = new CEventImageFragment();
                cEventImageFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, cEventImageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    /**
     * Checks the data entered is complete; otherwise, will show a popup and require field to be
     * filled in properly.
     *
     * @author Kyle Zwarich
     */
    public void checkField(View field){
        if (field instanceof TextView){
            String thisText = ((TextView) field).getText().toString();
            if (thisText.length() <= 1){
                Log.d("PREVIEW", "Event field " + field + " missing entry.");
            }
        }
        else if (field instanceof ImageView){
            Log.d("PREVIEW", "Event image missing");
        }
    }
}