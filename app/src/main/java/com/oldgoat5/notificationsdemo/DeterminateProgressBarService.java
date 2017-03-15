package com.oldgoat5.notificationsdemo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

public class DeterminateProgressBarService extends IntentService {

    private static final String SERVICE_NAME = "determinate_service";

    public DeterminateProgressBarService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final int ID_DETERMINATE_SERVICE = 9001;
        final int ID_NOTIFICATION_COMPLETE = 300;
        final int MAX_PROGRESS = 250;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("ProgressBarTitle")
                .setContentText("ProgressBarText")
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        int progress = 0;
        notification.setProgress(MAX_PROGRESS, progress, false);
        startForeground(ID_DETERMINATE_SERVICE, notification.build());

        do {
            try {
                Thread.sleep(15); //Do some work.
                progress++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notification.setProgress(MAX_PROGRESS, progress, false);
            notificationManager.notify(ID_DETERMINATE_SERVICE, notification.build());
        } while (progress < MAX_PROGRESS);

        notification.setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setProgress(0, 0, false)
                .setLights(ContextCompat.getColor(
                        getApplicationContext(), R.color.colorPrimary), 1000, 1000)
                .setVibrate(new long[]{800, 800, 800, 800})
                .setDefaults(Notification.DEFAULT_SOUND);
        notificationManager.notify(ID_NOTIFICATION_COMPLETE, notification.build());

        stopSelf();
    }
}
