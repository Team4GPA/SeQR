package com.example.seqr;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.controllers.EventController;
import com.example.seqr.models.Announcement;
import com.example.seqr.models.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AnnouncementDetailFragment extends Fragment {

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