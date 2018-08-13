package com.example.cgaima.squaa;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.Message;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.Models.User;
import com.example.cgaima.squaa.activities.HomeActivity;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);



        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Message.class);

        ParseObject.registerSubclass(EventAttendance.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("squID")
                .clientKey("christinechiticheyenne")
                .server("http://squaa.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);

        //configure the channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("myChannelId", "My Channel", importance);
        channel.setDescription("Reminders");

        //register the channel with the notifications manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);

        createNotification(4, R.drawable.calendar, "Friendly Reminder", "Open squaa to view all of the events happening near you!");
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