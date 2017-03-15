package com.oldgoat5.notificationsdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_USERS_REPLY = "key_users_reply";
    public static final int ID_DIRECT_REPLY_NOTIFICATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateResources();
    }

    /*****************************************************************
     * We need one Notification to be the group summary Notification.
     *         Use .setGroupSummary(true) for this Notification.  On
     *         lower than SDK 24, the group summary notification is the
     *         one that will be shown.
     *****************************************************************/
    private void bundledInboxStyleNotifications() {
        final String KEY_NOTIFICATION_GROUP = "key_notification_group";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, 0);

        Notification notification0 = new NotificationCompat.Builder(this)
                .setGroup(KEY_NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        android.R.drawable.ic_dialog_email))
                .setContentTitle("Bundled Notifications Content Title")
                .setContentText("Content Text for group summary")
                .setStyle(new NotificationCompat.InboxStyle()
                        .setSummaryText("This is my inbox style summary."))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setLights(ContextCompat.getColor(
                        getApplicationContext(), R.color.willowtree_text), 1000, 1000)
                .setVibrate(new long[]{800, 800, 800, 800})
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();

        notificationManager.notify(0, notification0);

        Notification notification1 = new NotificationCompat.Builder(this)
                .setGroup(KEY_NOTIFICATION_GROUP)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark))
                .setContentTitle("Toor Wolliw")
                .setContentText("!ppa na Desaeler")
                .build();

        notificationManager.notify(1, notification1);

        Notification notification2 = new NotificationCompat.Builder(this)
                .setGroup(KEY_NOTIFICATION_GROUP)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark))
                .setContentTitle("Willow Root")
                .setContentText("Released an app!")
                .build();

        notificationManager.notify(2, notification2);

        Notification notification3 = new NotificationCompat.Builder(this)
                .setGroup(KEY_NOTIFICATION_GROUP)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark))
                .setContentTitle("Daniel WillowTree")
                .setContentText("Released an app!")
                .build();

        notificationManager.notify(3, notification3);

        Notification notification4 = new NotificationCompat.Builder(this)
                .setGroup(KEY_NOTIFICATION_GROUP)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark))
                .setContentTitle("This style is useful for ")
                .setContentText("displaying multiple single lines.")
                .build();

        notificationManager.notify(4, notification4);
    }

    /*****************************************************************
     * PendingIntent - A PendingIntent contains an Intent that we want
     *         to run in the future.  In our case, we either want to
     *         start a Service or start an Activity (depending on
     *         SDK version).  It allows the receiving entity to execute
     *         the Intent with the same permissions as <em>this</em>
     *         entity.
     *
     * Action - Action buttons can be added to notifications and they
     *         will fire our PendingIntent.
     *
     * RemoteInput - A RemoteInput takes input from a user and passes
     *         it to our Action's PendingIntent under the key we
     *         specify.
     *         {@link RemoteInput#getResultsFromIntent} is used to
     *         retrieve the input.
     *
     * Direct Reply is a feature of Android Nougat SDK 24+.  On devices
     *         below 24, we want to keep the same end functionality.
     *         On 24+ this Notification starts a Service to handle the
     *         input.  On lower versions, it starts an Activity where
     *         the user can enter their input.
     *****************************************************************/
    private void directReplyNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent serviceIntent = new Intent(this, ReplyService.class);
        PendingIntent servicePendingIntent = PendingIntent.getService(
                this, 0, serviceIntent, PendingIntent.FLAG_ONE_SHOT);

        /*************************************************************
         * We must manually cancel a notification when it is
         * opened from an action.
         *************************************************************/
        Intent activityIntent = new Intent(this, ReplyActivity.class);
        activityIntent.putExtra(ReplyActivity.KEY_NOTIFICATION_ID, ID_DIRECT_REPLY_NOTIFICATION);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(
                this, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_USERS_REPLY)
                .setLabel(getString(R.string.reply_label))
                .build();

        NotificationCompat.Action remoteInputAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_dialog_email, getString(R.string.reply_label),
                isNougat() ? servicePendingIntent : activityPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        Notification directReplyNotification = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(activityPendingIntent)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("My Direct Response Notification")
                .setContentText("Daniel: Would you like to send a message?")
                .addAction(remoteInputAction)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setLights(ContextCompat.getColor(
                        getApplicationContext(), R.color.colorPrimary), 1000, 1000)
                .setVibrate(new long[]{800, 800, 800, 800})
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();

        notificationManager.notify(ID_DIRECT_REPLY_NOTIFICATION, directReplyNotification);
    }

    private void bigTextStyleNotification() {
        final int ID = 100;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("BigTextContentTitle")
                        .setSummaryText("Top Summary Line")
                        .bigText("BigText: This style is useful if you need to display a large " +
                                "body of text.  When this notification is collapsed (when " +
                                "there are also other notifications in the shade or if it is " +
                                "manually collapsed), it will " +
                                "only show this notification's .setContentTitle() and " +
                                ".setContentText()."))
                .setContentTitle("BigTextStyle ContentTitle")
                .setContentText("BigTextStyle ContentText")
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setLights(ContextCompat.getColor(
                        getApplicationContext(), R.color.colorPrimary), 1000, 1000)
                .setVibrate(new long[]{800, 800, 800, 800})
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();

        notificationManager.notify(ID, notification);
    }

    private void bigPictureStyleNotification() {
        final int ID = 200;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_menu_gallery)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle("BigPictureContentTitle")
                        .setSummaryText("BigPictureSummaryText")
                        .bigLargeIcon(BitmapFactory.decodeResource(getResources(),
                                android.R.mipmap.sym_def_app_icon))
                        .bigPicture(BitmapFactory.decodeResource(getResources(),
                                android.R.mipmap.sym_def_app_icon)))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        android.R.drawable.ic_menu_gallery))
                .setContentTitle("BigPictureStyle ContentTitle")
                .setContentText("BigPictureStyle ContentText")
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setLights(ContextCompat.getColor(
                        getApplicationContext(), R.color.colorPrimary), 1000, 1000)
                .setVibrate(new long[]{800, 800, 800, 800})
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();

        notificationManager.notify(ID, notification);
    }

    private void determinateProgressBarNotification() {
        startService(new Intent(getApplicationContext(), DeterminateProgressBarService.class));
    }

    /*****************************************************************
     * To set an Indeterminate {@link android.widget.ProgressBar},
     *         use .setProgress(0, 0, false).
     *****************************************************************/
    private void indeterminateProgressBarNotification() {
        startService(new Intent(getApplicationContext(), IndeterminateProgressBarService.class));
    }

    private void instantiateResources() {
        Button directReplyNotificationButton = (Button)
                findViewById(R.id.direct_reply_notification_button);
        directReplyNotificationButton.setOnClickListener(v -> directReplyNotification());

        Button bundledInboxStyleButton = (Button)
                findViewById(R.id.bundled_inbox_style_button);
        bundledInboxStyleButton.setOnClickListener(v -> bundledInboxStyleNotifications());

        Button bigTextStyleButton = (Button)
                findViewById(R.id.big_text_style_button);
        bigTextStyleButton.setOnClickListener(v -> bigTextStyleNotification());

        Button bigPictureStyleButton = (Button)
                findViewById(R.id.big_picture_style_button);
        bigPictureStyleButton.setOnClickListener(v -> bigPictureStyleNotification());

        Button progressBarDeterminateButton = (Button)
                findViewById(R.id.determinate_progress_bar_button);
        progressBarDeterminateButton.setOnClickListener(
                v -> determinateProgressBarNotification());

        Button progressBarIndeterminateButton = (Button)
                findViewById(R.id.indeterminate_progress_bar_button);
        progressBarIndeterminateButton.setOnClickListener(
                v -> indeterminateProgressBarNotification());

        ImageView attributionImageView = (ImageView) findViewById(R.id.wt_attribution);
        attributionImageView.setOnClickListener(l ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                        getString(R.string.attribution_url)))));
    }

    private boolean isNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
