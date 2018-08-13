package com.example.cgaima.squaa;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.cgaima.squaa.activities.HomeActivity;

@SuppressLint("Registered")
public class NotifEnabler {

    private Context mContext;

    public NotifEnabler(Context context) {
        mContext = context.getApplicationContext();
    }

    public  void scheduleNotification(long delay, int notificationId, String title, String body) {//delay is after how much time(in millis) from current time you want to schedule the notification

        Intent intent = new Intent(mContext, HomeActivity.class);
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; //cancel old intent and create new one
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notifs with same NotifId
        PendingIntent activity = PendingIntent.getActivity(mContext, requestID, intent, flags);

        Notification notif = new NotificationCompat.Builder(mContext, "myChannelId")
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.map)
                .setContentIntent(activity)
                .build();

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(mContext);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId, notif); //updtae the notif later


        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, activity);
    }
}
