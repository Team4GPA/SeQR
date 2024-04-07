package com.example.seqr.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.google.firebase.Timestamp;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seqr.MainActivity;
import com.example.seqr.R;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

/**
 * A fragment for adding event details.
 */
public class CEventDetailFragment extends Fragment {
    boolean limitCapacity = false;
    private Calendar cal;
    private int selectedHour;
    private int selectedMinute;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private TextView eventDateInput;
    private TextView eventTimeInput;
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
        EditText eventLocationInput = view.findViewById(R.id.eventLocationInput);
        EditText eventDescriptionInput = view.findViewById(R.id.eventDescriptionInput);
        EditText capacityInput = view.findViewById(R.id.eventCapacityInput);
        CheckBox hasCapacity = view.findViewById(R.id.hasCapacity);
        eventDateInput = view.findViewById(R.id.eventDateInput);
        eventTimeInput = view.findViewById(R.id.eventTimeInput);


        eventTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog();
            }
        });
        eventDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });
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
                Date eventStartTime = cal.getTime();
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
                bundle.putSerializable("eventTime", eventStartTime);
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

    private void openDatePickerDialog() {
        cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                getContext(), R.style.CustomDateTimePickerDialogStyle, (view1, year1, monthOfYear1, dayOfMonth1) -> {
            selectedDay = dayOfMonth1;
            selectedMonth = monthOfYear1;
            selectedYear = year1;
            cal.set(Calendar.YEAR, selectedYear);
            cal.set(Calendar.MONTH, selectedMonth);
            cal.set(Calendar.DAY_OF_MONTH, selectedYear);
            eventDateInput.setText(String.format(Locale.CANADA, "%d-%d-%d", dayOfMonth1, monthOfYear1 + 1, year1));
        }, year, month, day);

        datePickerDialog.show();
    }

    private void openTimePickerDialog() {
        if (cal == null) {
            Toast.makeText(getContext(), "Please select the date first", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.CustomDateTimePickerDialogStyle,(view, hourOfDay, minuteOfHour) -> {
            selectedHour = hourOfDay;
            selectedMinute = minuteOfHour;
            cal.set(Calendar.HOUR_OF_DAY, selectedHour);
            cal.set(Calendar.MINUTE, selectedMinute);
            eventTimeInput.setText(String.format(Locale.CANADA, "%d:%d", hourOfDay, minuteOfHour));
        }, hour, minute, true);

        timePickerDialog.show();
    }
}