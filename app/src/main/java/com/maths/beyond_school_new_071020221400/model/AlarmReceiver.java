package com.maths.beyond_school_new_071020221400.model;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.maths.beyond_school_new_071020221400.MainActivity;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, UtilityFunctions.getPendingIntentFlag());

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MediaPlayer mp = Objects.requireNonNull(MediaPlayer.create(context, uri));
        mp.start();

        // function to set custom notification tone


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Beyond_school")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Beyond School")
                .setContentText("It's time to practice Tables.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(123, builder.build());
    }
}
