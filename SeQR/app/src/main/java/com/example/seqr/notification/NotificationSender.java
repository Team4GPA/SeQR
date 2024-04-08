package com.example.seqr.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Utility class for sending notifications and milestones via Firebase Cloud Messaging (FCM).
 */
public class NotificationSender {
    /**
     * Sends a notification to the specified device using FCM.
     *
     * @param title         The title of the notification.
     * @param description   The description of the notification.
     * @param announcementID The ID of the announcement.
     * @param eventID       The ID of the event associated with the announcement.
     * @param fcmToken      The FCM token of the device to send the notification to.
     */
    public static void sendNotification(String title, String description, String announcementID, String eventID, String fcmToken) {
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", description);

            JSONObject dataObj = new JSONObject();
            dataObj.put("announcementID", announcementID);
            dataObj.put("eventID", eventID);
            dataObj.put("ifNotification", true);

            jsonObject.put("notification", notificationObj);
            jsonObject.put("data", dataObj);
            jsonObject.put("to", fcmToken);

            callAPI(jsonObject);

        }
        catch (Exception e) {
            Log.d("notfi", "sendNotification failed");
        }
    }
    /**
     * Sends a milestone to the specified device using FCM.
     *
     * @param title     The title of the milestone.
     * @param description   The description of the milestone.
     * @param eventID       The ID of the event associated with the milestone.
     * @param fcmToken  The FCM token of the device to send the milestone to.
     */
    public static void sendMilestone(String title, String description, String eventID, String fcmToken) {
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", description);

            JSONObject dataObj = new JSONObject();
            dataObj.put("eventID", eventID);
            dataObj.put("ifNotification", false);

            jsonObject.put("notification", notificationObj);
            jsonObject.put("data", dataObj);
            jsonObject.put("to", fcmToken);

            callAPI(jsonObject);

        }
        catch (Exception e) {
            Log.d("notfi", "sendMilestone failed");
        }
    }

    /**
     * Makes a network call to the FCM API with the provided JSON object.
     *
     * @param jsonObject The JSON object containing notification/milestone details and the FCM token.
     */
    private static void callAPI(JSONObject jsonObject) {
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
