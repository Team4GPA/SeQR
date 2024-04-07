package com.example.seqr.administrator;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.seqr.events.DeleteEventFragment;
import com.example.seqr.R;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * A fragment for editing events in the admin dashboard.
 */
public class AEditEventFragment extends Fragment {
    TextView eventNameTextView;
    TextView eventOrganizerTextView;
    TextView eventLocationTextView;
    TextView eventTimeTextView;
    TextView eventCapacityTextView;
    TextView eventDescriptionTextView;
    ImageView eventImageView;

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
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout of fragment and create buttons
        View view =  inflater.inflate(R.layout.fragment_a_edit_event, container, false);
        Button deleteProfileButton = view.findViewById(R.id.admin_event_edit_delete_button);
        Button backButton = view.findViewById(R.id.admin_edit_event_back_button);

        eventNameTextView = view.findViewById(R.id.admin_event_edit_name);
        eventOrganizerTextView = view.findViewById(R.id.admin_event_edit_organizer);
        eventLocationTextView = view.findViewById(R.id.admin_event_edit_location);
        eventTimeTextView = view.findViewById(R.id.admin_event_edit_time);
        eventCapacityTextView = view.findViewById(R.id.admin_event_edit_capacity);
        eventDescriptionTextView = view.findViewById(R.id.admin_event_edit_description);
        eventImageView = view.findViewById(R.id.admin_event_edit_photo);

        Bundle bundle = getArguments();
        assert bundle != null;
        String organizerName = bundle.getString("organizer","");
        String eventName = bundle.getString("eventName", "");
        String eventLocation = bundle.getString("location", "");
        Date eventTime = (Date) bundle.getSerializable("eventStartTime");
        String eventCapacity = bundle.getString("maxCapacity", "");
        String eventDescription = bundle.getString("eventDesc", "");
        String eventID = bundle.getString("eventID", "");

        eventOrganizerTextView.setText(organizerName);
        eventNameTextView.setText(eventName);
        eventLocationTextView.setText(eventLocation);
        eventTimeTextView.setText(eventTime.toString());
        eventCapacityTextView.setText(eventCapacity);
        eventDescriptionTextView.setText(eventDescription);

        // Convert the imageID into an event poster and replace the image view with it
        String path = Uri.encode("EventPosters/"+eventID+".jpg");
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Uri eventImageUri = Uri.parse(imageUrl);
        Picasso.get().load(eventImageUri).into(eventImageView);

        // Handle pressing back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        // Handle pressing delete button
        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("eventID", eventID);
                showConfirmationDialog(bundle1);
            }
        });

        return view;
    }
    /**
     * Show confirmation dialog for deletion.
     * @param bundle A bundle with the event ID for the event
     */
    private void showConfirmationDialog(Bundle bundle) {
        DeleteEventFragment dialogFragment = new DeleteEventFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getChildFragmentManager(), "DeleteEventFragment");
    }


}

