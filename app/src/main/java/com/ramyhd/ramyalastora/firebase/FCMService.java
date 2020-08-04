package com.ramyhd.ramyalastora.firebase;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.RamiActivities.FromNotification;
import com.ramyhd.ramyalastora.interfaces.Constants;

import java.util.Map;
import java.util.Objects;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(Constants.LOG + "fcm", "From: " + remoteMessage.getFrom());

        // Check if message Constants a data payload.

        Log.d(Constants.LOG + "fcm", "Message data payload: " + remoteMessage.toString());
        Log.d(Constants.LOG + "fcm", remoteMessage.getData().toString());
        Object o = remoteMessage.getData().get("message");
        Log.d(Constants.LOG + "fcm", "data details : " + remoteMessage.getData().get("details"));
        Log.d(Constants.LOG + "fcm", "notification details : " + Objects.requireNonNull(remoteMessage.getNotification()).getBody());
        Log.d(Constants.LOG + "fcm", "remoteMessage.getNotification()).getBody() = "
                + Objects.requireNonNull(remoteMessage.getNotification()).getBody());

        Log.d(Constants.LOG + "fcm", "remoteMessage.getData().get(notificationId != null");
        // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
        Map<String, String> data = remoteMessage.getData();
        Log.d(Constants.Log + "fcmdata", "title = " + Objects.requireNonNull(data.get("title")) +
                " | id = " + data.get("id") +
                " | image = " + data.get("image")+
                " | news_id = "+data.get("news_id"));
        Log.d(Constants.LOG + "fcm", "Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT");
        showNotification(data.get("title"),
                data.get("body"),
                Integer.parseInt(Objects.requireNonNull(data.get("id"))),
                data.get("news_id"),
                data.get("type"),
                data.get("image"));

        // Check if message Constants a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(Constants.LOG + "fcm", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private void showNotification(String title, String message, int notifi_id, String newsId, String type, String Newsimage) {
        Log.d(Constants.LOG + "fcm", "showNotification");
        Intent intent = new Intent(this, FromNotification.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (newsId != null && type.equals("KEY_NEWS")) {
            intent.putExtra("data", "from_notification");
            intent.putExtra("newsId", newsId);
            Log.d(Constants.Log + "newsId", "fcm newsId = " + newsId);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = null;
        try {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert manager != null;
            createChannel(manager);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "FileDownload")
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logotoolbar)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#E0BD55"))
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Objects.requireNonNull(manager).notify(notifi_id, builder.build());
        startForeground(1, builder.build());
    }

    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        Log.d(Constants.LOG + "fcm", "createChannel");

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        String name = "FileDownload";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel mChannel = new NotificationChannel(name, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setSound(alarmSound, attributes);
        mChannel.enableVibration(true);
        notificationManager.createNotificationChannel(mChannel);
    }

    /*@Override
    public void onNewToken(String token) {
        Log.d(Constants.LOG+"refresh_token", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        APIInterface apiInterface = null;
        Call<ErrorBody> call = apiInterface.deviceToken(token);
        call.enqueue(new Callback<ErrorBody>() {
            @Override
            public void onResponse(Call<ErrorBody> call, Response<ErrorBody> response) {

                ErrorBody resource = response.body();

                String status = response.body().getStatus().getMessage();
                String code = response.code()+"";

                Log.d(Constants.LOG+"ErrorBody", "Status = "+status+" | Code = "+code);

                if (status.equals(Constants.message)) {

                    Log.d(Constants.LOG+"device_token", resource.getErrorData().getDeviceToken());

                }


            }

            @Override
            public void onFailure(Call<ErrorBody> call, Throwable t) {
                call.cancel();
            }
        });
    }*/
}
