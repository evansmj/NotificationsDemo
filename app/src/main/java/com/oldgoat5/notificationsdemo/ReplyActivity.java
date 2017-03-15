package com.oldgoat5.notificationsdemo;

import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ReplyActivity extends AppCompatActivity {

    public static final String KEY_NOTIFICATION_ID = "key_notification_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        /*************************************************************
         * We must manually cancel a notification when it is
         * opened from an action.
         *************************************************************/
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(getIntent().getExtras().getInt(KEY_NOTIFICATION_ID));

        instantiateResources();
    }

    private void instantiateResources() {
        ImageView attributionImageView = (ImageView) findViewById(R.id.wt_attribution);
        attributionImageView.setOnClickListener(l ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                        getString(R.string.attribution_url)))));
    }
}
