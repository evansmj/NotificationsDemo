package com.oldgoat5.notificationsdemo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

public class IndeterminateProgressBarService extends IntentService {

    private static final String SERVICE_NAME = "indeterminate_service";

    public IndeterminateProgressBarService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final int ID_INDETERMINATE_SERVICE = 9000;
        final int ID_NOTIFICATION_COMPLETE = 400;
        final int MAX_PROGRESS = 250;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, MainActivity.ID_DEFAULT_CHANNEL)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("ProgressBarTitle")
                .setContentText("ProgressBarText")
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark))
                .setColorized(true); //Added in Android O

        int progress = 0;
        notification.setProgress(0, 0, true);
        startForeground(ID_INDETERMINATE_SERVICE, notification.build());

        do {
            try {
                Thread.sleep(15); //Do some work.
                progress++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (progress < MAX_PROGRESS);

        notification.setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setProgress(0, 0, false)
                .setContentText("Indeterminate ProgressBar finished.")
                .setLights(ContextCompat.getColor(
                        getApplicationContext(), R.color.colorPrimary), 1000, 1000)
                .setVibrate(new long[]{800, 800, 800, 800})
                .setDefaults(Notification.DEFAULT_SOUND);

        /*************************************************************
         * Our startForeground() notification will be removed when we
         * call stopSelf(), so we supply a new ID to create a notification
         * we want persisted.
         *************************************************************/
        notificationManager.notify(ID_NOTIFICATION_COMPLETE, notification.build());

        stopSelf();
    }
}
