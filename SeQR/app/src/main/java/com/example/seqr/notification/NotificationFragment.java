package com.example.seqr.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seqr.R;
import com.example.seqr.adapters.AnnouncementAdapter;
import com.example.seqr.announcements.AnnouncementDetailFragment;
import com.example.seqr.controllers.AnnouncementController;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.Announcement;
import com.example.seqr.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private AnnouncementAdapter notificationAdapter;
    private List<Announcement> notificationList;
    private String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        id = bundle.getString("id","");

        recyclerView = view.findViewById(R.id.notificationRecyclerview);
        notificationList = new ArrayList<>();
        notificationAdapter = new AnnouncementAdapter(notificationList, this::notificationClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notificationAdapter);
        Button notificationBackButton = view.findViewById(R.id.notificationBackButton);
        notificationBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        ProfileController profileController = new ProfileController();
        AnnouncementController announcementController = new AnnouncementController();

        profileController.getNotificationsByUUID(id, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot profileDoc = task.getResult();
                    List<String> notifications = (List<String>) profileDoc.get("notifications");
                    if (notifications != null && !notifications.isEmpty()) {
                        for (String announcementID : notifications) {
                            announcementController.getAnnouncementById(announcementID, new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot notificationDoc = task.getResult();
                                        if (notificationDoc != null && notificationDoc.exists()) {
                                            Announcement notification = notificationDoc.toObject(Announcement.class);
                                            if (notification != null) {
                                                notificationList.add(notification);
                                                Collections.sort(notificationList, (a1, a2) -> a2.getTime().compareTo(a1.getTime()));
                                                notificationAdapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            Log.d("DEBUG", "notification does not exist in firebase" + announcementID);
                                        }
                                    }
                                    else {
                                        Log.d("DEBUG", "There was some error with getting the notification");
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        return view;
    }

    private void notificationClicked(Announcement notification) {
        Bundle bundle = new Bundle();
        bundle.putString("announcementID", notification.getAnnouncementID());
        bundle.putString("eventID", notification.getEventID());
        bundle.putBoolean("ifAttendee", true);

        AnnouncementDetailFragment announcementDetailFragment = new AnnouncementDetailFragment();
        announcementDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, announcementDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}