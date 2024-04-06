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
import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                                                    String fcmToken = (String) profileDoc.get("fcmToken");
                                                    if (notifications == null) {
                                                        notifications = new ArrayList<>();
                                                    }
                                                    notifications.add(announcementID);
                                                    profileController.notificationsUpdater(id, notifications);
                                                    sendNotification(title, description, announcementID, eventID, fcmToken);
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

    private void sendNotification(String title, String description, String announcementID, String eventID, String fcmToken) {
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", description);

            JSONObject dataObj = new JSONObject();
            dataObj.put("announcementID", announcementID);
            dataObj.put("eventID", eventID);

            jsonObject.put("notification", notificationObj);
            jsonObject.put("data", dataObj);
            jsonObject.put("to", fcmToken);

            callAPI(jsonObject);

        }
        catch (Exception e) {
            Log.d("notfi", "sendNotification failed");
        }
    }

    private void callAPI(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAFkMy8Hw:APA91bFc3lJBNwZLKXqq_Es-o7Mgw-qhqGS99iTFV5DPOxjqVGntVUjz7GisFv3SSDzAxKXBwxXgPDc-Wr4iZG5cdAWYva3xaNxrmC6aFXdFywvTgQzP94V25McJZr9eK2DL-T_A6gu0")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("notfi", "Call back failed");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("notfi", "Call back succeed");
            }
        });
    }
}