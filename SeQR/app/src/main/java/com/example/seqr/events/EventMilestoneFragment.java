package com.example.seqr.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.seqr.R;
import com.example.seqr.controllers.EventController;
import com.example.seqr.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Fragment to Display all of an Event's Milestones
 */
public class EventMilestoneFragment extends Fragment {

    /**
     * Method to handle creating a view for the fragment and button clicks
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event_milestone, container, false);

        Button eventMilestoneBackButton = view.findViewById(R.id.eventMilestoneBackButton);
        ImageView milestoneImageView1 = view.findViewById(R.id.milestoneImageView1);
        ImageView milestoneImageView2 = view.findViewById(R.id.milestoneImageView2);
        ImageView milestoneImageView3 = view.findViewById(R.id.milestoneImageView3);
        ImageView milestoneImageView4 = view.findViewById(R.id.milestoneImageView4);

        Bundle bundle = getArguments();
        assert bundle != null;
        String eventId = bundle.getString("eventID","");

        EventController eventController = new EventController();
        eventController.getEventById(eventId, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot eventDoc = task.getResult();
                    if(eventDoc != null && eventDoc.exists()) {
                        Event event = eventDoc.toObject(Event.class);
                        int milestoneAlert = event.getMilestoneAlert();
                        if (milestoneAlert == 1) {
                            milestoneImageView1.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView2.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                            milestoneImageView3.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                            milestoneImageView4.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                        }
                        else if (milestoneAlert == 2) {
                            milestoneImageView1.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView2.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView3.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                            milestoneImageView4.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                        }
                        else if (milestoneAlert == 3) {
                            milestoneImageView1.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView2.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView3.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView4.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                        }
                        else if (milestoneAlert == 4) {
                            milestoneImageView1.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView2.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView3.setImageResource(R.drawable.baseline_milestone_completed_24);
                            milestoneImageView4.setImageResource(R.drawable.baseline_milestone_completed_24);
                        }
                        else {
                            milestoneImageView1.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                            milestoneImageView2.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                            milestoneImageView3.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                            milestoneImageView4.setImageResource(R.drawable.baseline_milestone_uncompleted_24);
                        }
                    }
                }
            }
        });
        eventMilestoneBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });



        return view;
    }
}