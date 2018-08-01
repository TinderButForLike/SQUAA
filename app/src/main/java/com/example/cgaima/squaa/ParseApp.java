package com.example.cgaima.squaa;

import android.app.Application;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.Message;
import com.example.cgaima.squaa.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {

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

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("squID")
                .clientKey("christinechiticheyenne")
                .server("http://squaa.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

    }
}