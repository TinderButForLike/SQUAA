package com.example.cgaima.squaa;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.activities.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FcmMessageHandler extends FirebaseMessagingService {
    private static final String TAG = "FcmMessagingService";
    private static final int MESSAGE_NOTIFICATION_ID = 10;
    List<Event> events;

    public FcmMessageHandler() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        events = new ArrayList<>();
        //configure the channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("myChannelId", "My Channel", importance);
        channel.setDescription("Reminders");

        //register the channel with the notifications manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        createNotification(MESSAGE_NOTIFICATION_ID, R.drawable.map, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void getPosts() {
        final Event.Query query = new Event.Query();
        query.getTop(); //queries the top 25 posts in parse
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        events.add(objects.get(i));
                    }
                } else {e.printStackTrace();}
            }
        });
    }


    private void checkForNotifs() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);

        for (int i = 0; i < events.size(); i++) {
            Date futureEvent = events.get(i).getDate("event_date");
            if (futureEvent.after(today)) {
                Log.d("It's before today", futureEvent.toString());
            }
        }
    }

    private void createNotification(int nId, int iconRes, String title, String body) {

        //intent trigger when something is selected
        Intent intent = new Intent(this, HomeActivity.class);
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notifs with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; //cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags); //gets an activity through its intent

        //attach the pending intent to a new notif
        Notification mBuilder =
                new NotificationCompat.Builder(this, "myChannelId")
                        .setSmallIcon(iconRes)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true) //hides notif after selection
                        .setWhen(System.currentTimeMillis())
                        .build();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, mBuilder); //updtae the notif later

    }


}




