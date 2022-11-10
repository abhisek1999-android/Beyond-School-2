package com.maths.beyond_school_new_071020221400.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.maths.beyond_school_new_071020221400.R;

public class StickyNotification {

    private static final int NOTIFICATION_ID = 140;
    Context mContext;
    NotificationManager mNotificationManager;
    Class table_with_hintClass;
    String desc;


    private static final String CHANNEL_ID="Default Channel Beyond School";
    private static final String CHANNEL_NAME="Incoming Call Channel";
    private static final String CHANNEL_DESC="Channel for incoming call";

    public StickyNotification(Context mContext, Class table_with_hintClass,String desc) {
        this.mContext = mContext;
        this.desc=desc;
        this.table_with_hintClass=table_with_hintClass;
        mNotificationManager = (NotificationManager)
        mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public void makeNotification() {

        Intent intent = new Intent(mContext,table_with_hintClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent,PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(mContext,CHANNEL_ID)
                        .setSmallIcon(R.drawable.play_button)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                                R.drawable.select_option_ct))
                        .setContentTitle("Beyond School")
                        .setContentText("Beyond School "+desc)
                        .setContentIntent(pendingIntent)
                        .setSound(null)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOngoing(true);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(1,mBuilder.build());
    }
}

