package com.example.seqr.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.seqr.R;
import com.example.seqr.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.checkerframework.checker.units.qual.N;

/**
 * Service for handling incoming push notifications.
 */
public class PushNotificationService extends FirebaseMessagingService {

    /**
     * Empty constructor method
     */
    public PushNotificationService() {
    }

    /**
     * Called when a new FCM token is generated.
     *
     * @param token The new FCM token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("Token", token);
        // update server
    }

    /**
     * Called when a new message is received.
     *
     * @param remoteMessage The message received from FCM.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("notif", "Message notification payload: " + remoteMessage.getNotification());
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        if (remoteMessage.getData().size() > 0) {
            String ifNotificationString = remoteMessage.getData().get("ifNotification");
            Boolean ifNotification = Boolean.parseBoolean(ifNotificationString);
            String eventID = remoteMessage.getData().get("eventID");
            if (ifNotification) {
                String announcementID = remoteMessage.getData().get("announcementID");
                // respond to received message
                displayNotification(title, body, eventID, announcementID);
            }
            else {
                displayNotification(title, body, eventID, null);
            }
        }
    }

    /**
     * Displays a notification with the given title, body, event ID, and announcement ID (if applicable).
     *
     * @param title         The title of the notification.
     * @param body          The body of the notification.
     * @param eventID       The ID of the event associated with the notification.
     * @param announcementID The ID of the announcement (if applicable).
     */
    private void displayNotification(String title, String body, String eventID, @Nullable String announcementID) {
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.seqr_icon_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setAutoCancel(true);

        Intent intent = new Intent(this, SplashActivity.class);
        if (announcementID != null) {
            intent.putExtra("announcementID", announcementID);
            intent.putExtra("ifNotification", true);
        }
        else {
            intent.putExtra("ifNotification", false);
        }
        intent.putExtra("eventID", eventID);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Default", "Default", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            int lightColor = ContextCompat.getColor(this, R.color.theme_primary);
            channel.setLightColor(lightColor);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You don't have permission to post notifications", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        notificationManager.notify(0, builder.build());
    }


}