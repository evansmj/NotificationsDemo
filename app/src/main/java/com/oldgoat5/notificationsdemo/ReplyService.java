package com.oldgoat5.notificationsdemo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;

public class ReplyService extends IntentService {

    public static final String KEY_NETWORK_TRANSMISSION_SERVICE =
            "key_network_transmission_service";

    public ReplyService() {
        super(KEY_NETWORK_TRANSMISSION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String response = remoteInput.getString(MainActivity.KEY_USERS_REPLY);
            //Send the user response to the server
        }

        //Update (or dismiss) the notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentText(getString(R.string.response_sent))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .build();

        notificationManager.notify(MainActivity.ID_DIRECT_REPLY_NOTIFICATION, notification);
    }
}
