package com.example.cgaima.squaa.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.R;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity {

    //resource variables
    boolean joined;
    private static final String TAG = "lyft:Example";
    private static final String LYFT_PACKAGE = "me.lyft.android";
    private static final String CLIENT_ID = "rzupE13z8Yo2";
    private static final String CLIENT_TOKEN = "4IA9raWjUI3rr3igs0SNcIEzrvWmUhl8EAZGaajBtVeEGrg7CBj+tzqmri6pDEP2yC3QN/D/23/Bc6Ew0DEX5IfLXfJv0bZt2JYBNIf1aNeXazxXU8T32NU=";

    FloatingActionButton fab;
    Event event;

    @BindView(R.id.ivEventPic) ImageView EventPic;
    @BindView(R.id.tvEventName) TextView EventName;
    //    @BindView(R.id.tvDate)
//    TextView date;
    @BindView(R.id.tvDescription) TextView description;
    @BindView(R.id.tvNumAttend) TextView numAttend;
    @BindView(R.id.tvOwner) TextView ownerName;
    @BindView(R.id.ivOwnerPic) ImageView ownerPic;
    @BindView(R.id.tvLocation) TextView location;
    @BindView(R.id.ratingBar) RatingBar rb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);


        Parcelable parcel = this.getIntent().getParcelableExtra("event");
        event = Parcels.unwrap(parcel);

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId(CLIENT_ID)
                .setClientToken(CLIENT_TOKEN)
                .build();

        LyftButton lyftButton = findViewById(R.id.lyft_button);
        lyftButton.setApiConfig(apiConfig);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Turn on permissions to call Lyft from our app!", Toast.LENGTH_SHORT).show();
            lyftButton.setVisibility(View.GONE); // hide lyft button
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ParseGeoPoint parseGeoPoint = event.getGeoPoint();

            RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                    .setPickupLocation(location.getLatitude(), location.getLongitude())
                    .setDropoffLocation(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
            rideParamsBuilder.setRideTypeEnum(RideTypeEnum.CLASSIC);
            lyftButton.setRideParams(rideParamsBuilder.build());
            lyftButton.load();
        }

        // set join event initial UI
        fab = findViewById(R.id.fab);
        joined = EventAttendance.isAttending(event);
        if (joined) { fab.setImageResource(R.drawable.ic_unjoin_event); }

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating,
                                        boolean fromUser) {
                event.put("rating", rating);
                event.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), String.format("Successfully rated event %.1f stars", rating),Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.e("EventDetailActivity", e.toString());
                            Toast.makeText(getApplicationContext(), "Failed to rate the event try again later",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        EventName.setText(event.getEventName());
        description.setText(event.getDescription());
        try {
            ownerName.setText(event.getOwner().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        location.setText(event.getLocation());
        int num = event.getAttendees().size();
        numAttend.setText(Integer.toString(num));

        Glide.with(this).load(event.getEventImage().getUrl()).into(EventPic);
        try {
            Glide.with(this).load(event.getOwner().fetchIfNeeded().getParseFile("profile_picture").getUrl()).into(ownerPic);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ownerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (event.getOwner().fetchIfNeeded().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                        Intent i = new Intent(EventDetailActivity.this, HomeActivity.class);
                        i.putExtra("profile", Parcels.wrap(event));
                        startActivity(i);
                    } else {
                        Intent i = new Intent(EventDetailActivity.this, HomeActivity.class);
                        i.putExtra("event_owner", Parcels.wrap(event));
                        startActivity(i);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void onJoin() {
        final boolean joined = EventAttendance.isAttending(event);
        // join event if not already joined
        if (!joined){
            final EventAttendance newEventAttendance = new EventAttendance();
            newEventAttendance.setEventAttendance(ParseUser.getCurrentUser(), event);
            newEventAttendance.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        fab.setImageResource(R.drawable.ic_unjoin_event);
                        numAttend.setText(String.valueOf(EventAttendance.getNumAttending(event)));
                        Log.d("EventAdapter", "Successfully joined event. :) ");
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to join event", Toast.LENGTH_LONG).show();
                        Log.e("EventAdapter", e.toString());
                    }
                }
            });
        }

        // unjoin event if already joined
        else {
            EventAttendance.Query query = new EventAttendance.Query();
            query.findEventAttendance(ParseUser.getCurrentUser(), event);
            try {
                fab.setImageResource(R.drawable.ic_join_event);
                query.getFirst().deleteInBackground();
                numAttend.setText(String.valueOf(EventAttendance.getNumAttending(event)));
                Log.d("EventAdapter", "Successfully unjoined event. ");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}