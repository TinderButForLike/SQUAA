package com.example.cgaima.squaa;

import android.app.Application;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(EventAttendance.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("squID")
                .clientKey("christinechiticheyenne")
                .server("http://squaa.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

    }
}