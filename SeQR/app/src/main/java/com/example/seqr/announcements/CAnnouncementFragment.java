package com.example.seqr.announcements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.seqr.R;
import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.models.Announcement;
import com.google.firebase.Timestamp;

import org.checkerframework.checker.units.qual.A;

import java.util.UUID;

public class CAnnouncementFragment extends Fragment {
    private String eventID;
    private String organizer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_announcement, container, false);

        Button cAnnouncementBackButton= view.findViewById(R.id.CAnnouncementBackButton);
        Button cAnnouncementConfirmButton= view.findViewById(R.id.CAnnouncementConfirmButton);
        EditText cAnnouncementTitleEditText = view.findViewById(R.id.CAnnouncementTitleEditText);
        EditText cAnnouncementDescEditText = view.findViewById(R.id.CAnnouncementDescEditText);
        CheckBox ifNotify = view.findViewById(R.id.ifNotify);

        Bundle bundle = getArguments();
        assert bundle != null;
        eventID = bundle.getString("eventID","");
        organizer = bundle.getString("organizer","");
        cAnnouncementBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        cAnnouncementConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = cAnnouncementTitleEditText.getText().toString();
                String description = cAnnouncementDescEditText.getText().toString();
                String announcementID = UUID.randomUUID().toString();
                Timestamp time = Timestamp.now();
                Announcement announcement = new Announcement(title, description, time, eventID, announcementID, organizer);

                AnnouncementController announcementController = new AnnouncementController();
                announcementController.addAnnouncement(announcement);

                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }
}