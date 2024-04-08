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
import android.widget.TextView;

import com.example.seqr.R;
import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.notification.NotificationSender;
import com.example.seqr.models.Announcement;
import com.example.seqr.models.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A class representing creating the fragment where we create an announcment
 */
public class CAnnouncementFragment extends Fragment {
    private String eventID;
    private String organizer;

    /**
     * Method that creates the view of the fragment and all associated logic
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the associated view of the fragment
     */
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
                if (!validAnnouncementDetails(cAnnouncementTitleEditText, cAnnouncementDescEditText)) {
                    return;
                }
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
                                                    String fcmToken = (String) profileDoc.get("fcmToken");
                                                    if (notifications == null) {
                                                        notifications = new ArrayList<>();
                                                    }
                                                    notifications.add(announcementID);
                                                    profileController.notificationsUpdater(id, notifications);
                                                    NotificationSender.sendNotification(title, description, announcementID, eventID, fcmToken);
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
    /**
     * Checks if a textview is empty
     * @param textView
     * @return a bool representing if the textview is empty
     */
    private Boolean ifTextViewNotEmpty(TextView textView) {
        if (textView.getText().toString().trim().isEmpty()) {
            textView.setError("Field input empty.");
            return false;
        }
        return true;
    }

    /**
     * Checks if all the textviews are not empty
     * @param textView1
     * @param textView2
     * @return returns true if none are empty
     */
    private Boolean validAnnouncementDetails(TextView textView1, TextView textView2) {
        return ifTextViewNotEmpty(textView1)
                && ifTextViewNotEmpty(textView2);
    }
}