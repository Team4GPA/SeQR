package com.example.seqr.announcements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.seqr.R;
import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Announcement;
import com.example.seqr.models.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
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

                if (ifNotify.isChecked()) {
                    EventController eventController = new EventController();
                    ProfileController profileController = new ProfileController();
                    eventController.getEventSignUps(eventID, new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot signUpDoc : task.getResult()) {
                                SignUp signUp = signUpDoc.toObject(SignUp.class);
                                if (signUp != null) {
                                    String id = signUp.getUserId();
                                    profileController.getNotificationsByUUID(id, new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot profileDoc = task.getResult();
                                                if (profileDoc != null && profileDoc.exists()) {
                                                    List<String> notifications = (List<String>) profileDoc.get("notifications");
                                                    if (notifications == null) {
                                                        notifications = new ArrayList<>();
                                                    }
                                                    notifications.add(announcementID);
                                                    profileController.notificationsUpdater(id, notifications);
                                                }
                                            } else {
                                                Log.d("DEBUG", "Error in getting the notifications");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }
}