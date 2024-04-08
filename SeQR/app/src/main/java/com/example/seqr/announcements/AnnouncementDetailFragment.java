package com.example.seqr.announcements;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.seqr.R;
import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.controllers.EventController;
import com.example.seqr.events.EventInfoFragment;
import com.example.seqr.models.Announcement;
import com.example.seqr.models.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A Fragment that displays the details of a given announcement
 */
public class AnnouncementDetailFragment extends Fragment {

    /**
     * Creates the given view for the fragment and associated logic to handle actions in the fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the view of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announcement_detail, container, false);

        TextView announcementDetailTitle = view.findViewById(R.id.AnnouncementDetailTitle);
        TextView announcementDetailOrganizer = view.findViewById(R.id.AnnouncementDetailOrganizer);
        TextView announcementDetailTime = view.findViewById(R.id.AnnouncementDetailTime);
        TextView eventInfoDescription = view.findViewById(R.id.eventInfoDescription);
        Button announcementDetailBackButton = view.findViewById(R.id.AnnouncementDetailBackButton);
        Button seeDetailButton = view.findViewById(R.id.seeDetailButton);

        Bundle bundle = getArguments();
        assert bundle != null;
        String announcementID = bundle.getString("announcementID","");
        String eventID = bundle.getString("eventID","");
        Boolean ifAttendee = bundle.getBoolean("ifAttendee", false);

        if (ifAttendee) {
            seeDetailButton.setVisibility(View.VISIBLE);
            seeDetailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("eventID",eventID);

                    EventInfoFragment eventInfoFragment = new EventInfoFragment();
                    eventInfoFragment.setArguments(bundle);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, eventInfoFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
                }
            });
        }
        else {
            seeDetailButton.setVisibility(View.GONE);
        }
        announcementDetailBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        AnnouncementController announcementController = new AnnouncementController();
        announcementController.getAnnouncementById(announcementID,task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    Announcement announcement = document.toObject(Announcement.class);

                    announcementDetailTitle.setText(announcement.getTitle());
                    announcementDetailOrganizer.setText(announcement.getOrganizer());
                    announcementDetailTime.setText(announcement.getTime().toDate().toString());
                    eventInfoDescription.setText(announcement.getDescription());
                }
                else{
                    //for part 4 add a dialog that says event does not exist and redirect to main page
                    Log.d("Debug","There wasn't a document with that id");
                }
            }else{
                Log.d("Debug", "error in retrieveing the document/announcement");
            }
        });

        return view;
    }
}