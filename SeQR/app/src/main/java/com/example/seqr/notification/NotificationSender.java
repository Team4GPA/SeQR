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

public class NotificationSender {
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
