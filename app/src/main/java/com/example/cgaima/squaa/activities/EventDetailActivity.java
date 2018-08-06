package com.example.cgaima.squaa.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
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
import com.example.cgaima.squaa.Models.GlideApp;
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

import java.util.Calendar;
import java.util.Date;

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

    Event event;
    EventAttendance eventAttendance;

    @BindView(R.id.ivEventPic) ImageView eventPic;
    @BindView(R.id.tvEventName) TextView eventName;
    @BindView(R.id.tvDate) TextView date;
    @BindView(R.id.tvDescription) TextView description;
    @BindView(R.id.tvNumAttend) TextView numAttend;
    @BindView(R.id.tvOwner) TextView ownerName;
    @BindView(R.id.ivOwnerPic) ImageView ownerPic;
    @BindView(R.id.tvLocation) TextView location;
    @BindView(R.id.ratingBar) RatingBar rb;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.tvRate) TextView tvRate;

    public EventDetailActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);


        Parcelable parcel = this.getIntent().getParcelableExtra("event");
        event = Parcels.unwrap(parcel);

        Parcelable parcel1 = this.getIntent().getParcelableExtra("eventAttendance");
        eventAttendance = Parcels.unwrap(parcel1);

        Calendar cal = Calendar.getInstance();
        Date today= cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date tmrw = cal.getTime();
        Date fromDate = event.getDate("fromDate");
        Date toDate = event.getDate("toDate");

        // if future event - allow user to join / unjoin and call event
        if (fromDate.after(today)) {
            // set join event initial UI
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            params.setBehavior(new FloatingActionButton.Behavior());
            params.setAnchorId(R.id.fab);
            fab.setLayoutParams(params);
            joined = EventAttendance.isAttending(event);
            if (joined) { fab.setImageResource(R.drawable.ic_unjoin_event); }
            numAttend.setText(String.valueOf(EventAttendance.getNumAttending(event)));
        }
        // if current event - allow user to call uber
        else if (fromDate.before(today) && toDate.after(today)) {
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
        }

        // if past event and user attended - allow user to rate within a day
        else if (toDate.after(today) && toDate.before(tmrw) && eventAttendance != null) {
            tvRate.setVisibility(View.VISIBLE);
            rb.setVisibility(View.VISIBLE);
            // set rating bar
            rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
                @Override
                public void onRatingChanged(RatingBar ratingBar, final float rating,
                                            boolean fromUser) {
                    int numRatings =  event.getInt("numRatings");
                    double avrgRating =  (numRatings * event.getDouble("rating") + rating ) / (numRatings + 1);
                    event.put("rating", avrgRating);
                    event.put("numRatings", numRatings+1);
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
        }

        // if past event and user didn't attend - show only event details
        else if (toDate.after(today) && toDate.before(tmrw) && eventAttendance == null) {
            Toast.makeText(this, "This event has already past and rating is not available yet. Check back in one day!", Toast.LENGTH_LONG).show();
        }

        // if past event after one day
        else {
            rb.setVisibility(View.VISIBLE);
            rb.setIsIndicator(true);
            rb.setStepSize((float) 0.5);

            int rating = event.getInt("rating");
            rb.setRating(rating);

            Toast.makeText(this, "This event has already past!", Toast.LENGTH_LONG).show();
        }


        // set all the other UI
        eventName.setText(event.getEventName());
        description.setText(event.getDescription());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String dateString = String.format("%s - %s", simpleDateFormat.format(fromDate), simpleDateFormat.format(toDate));
        date.setText(dateString);
        try {
            ownerName.setText(event.getOwner().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        location.setText(event.getLocation());
        int num = event.getAttendees().size();
        numAttend.setText(Integer.toString(num));

        try {
            Glide.with(this).load(event.getOwner().fetchIfNeeded().getParseFile("profile_picture").getUrl()).into(ownerPic);
            if (event.getEventImage() == null) {
                eventPic.setImageResource(R.drawable.image_default);
            }
            else {
                GlideApp.with(this)
                        .load(event.getEventImage().getUrl())
                        .error(R.drawable.image_default)
                        .into(eventPic);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ownerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (event.getOwner().fetchIfNeeded().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                        Intent i = new Intent(EventDetailActivity.this, HomeActivity.class);
                        i.putExtra("profile", Parcels.wrap(event));
                        startActivity(i);
                    } else {
                        Intent i = new Intent(EventDetailActivity.this, HomeActivity.class);
                        i.putExtra("eventOwner", Parcels.wrap(event));
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
                Log.d("EventAdapter", "Successfully unjoined event.");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}