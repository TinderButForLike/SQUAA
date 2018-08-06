package com.example.cgaima.squaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity {

    //resource variables
    boolean joined;
    private static final String TAG = "lyft:Example";
    private static final String CLIENT_ID = "rzupE13z8Yo2";
    private static final String LYFT_PACKAGE = "me.lyft.android";
    FloatingActionButton fab;

    @BindView(R.id.ivEventPic)
    ImageView EventPic;
    @BindView(R.id.ivChat)
    ImageView ChatIcon;
    @BindView(R.id.tvEventName)
    TextView EventName;
//    @BindView(R.id.tvDate)
//    TextView date;
    @BindView(R.id.tvDescription)
    TextView description;
    @BindView(R.id.tvNumAttend)
    TextView numAttend;
    @BindView(R.id.tvOwner)
    TextView ownerName;
    @BindView(R.id.ivOwnerPic)
    ImageView ownerPic;
    @BindView(R.id.tvLocation)
    TextView Eventlocal;
    @BindView(R.id.ratingBar1)
    RatingBar rb;
    @BindView(R.id.ivCal)
    ImageView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        Parcelable parcel = this.getIntent().getParcelableExtra("event");
        final Event event = (Event) Parcels.unwrap(parcel);

//        ApiConfig apiConfig = new ApiConfig.Builder()
//                .setClientId(CLIENT_ID)
//                .setClientToken("4IA9raWjUI3rr3igs0SNcIEzrvWmUhl8EAZGaajBtVeEGrg7CBj+tzqmri6pDEP2yC3QN/D/23/Bc6Ew0DEX5IfLXfJv0bZt2JYBNIf1aNeXazxXU8T32NU=")
//                .build();
//
//        LyftButton lyftButton = findViewById(R.id.lyft_button);
//        lyftButton.setApiConfig(apiConfig);
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        // check location permission
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Turn on permissions to call Lyft from our app!", Toast.LENGTH_LONG).show();
//            lyftButton.setVisibility(View.GONE); // hide lyft button
//            return;
//        }
//        else{
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            ParseGeoPoint parseGeoPoint = event.getGeoPoint();
//
//            RideParams.Builder rideParamsBuilder = new RideParams.Builder()
//                    .setPickupLocation(location.getLatitude(), location.getLongitude())
//                    .setDropoffLocation(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
//            rideParamsBuilder.setRideTypeEnum(RideTypeEnum.CLASSIC);
//            lyftButton.setRideParams(rideParamsBuilder.build());
//
//            lyftButton.load();
//        }

        fab = findViewById(R.id.fab);
        joined = false;
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_join));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!joined){
                joinEvent(event);
                numAttend.setText(Integer.toString(event.getAttendees().size()));
                joined = true;
                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_unjoin));
                }
                else{
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_join));
                    joined = false;
                    ParseUser current = ParseUser.getCurrentUser();
                    event.removeAll("attendees", Collections.singleton(current));
                    try {
                        event.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    numAttend.setText(Integer.toString(event.getAttendees().size()));
                }
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do google calendar stuff

            }
        });
        ChatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventDetailActivity.this, ChatActivity.class);
                i.putExtra("event_chat", Parcels.wrap(event));
                startActivity(i);
            }
        });


        // TODO - rating bar
//        rb =(RatingBar)findViewById(R.id.ratingBar1);
//        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating,
//                                        boolean fromUser) {
//                Toast.makeText(getApplicationContext(),Float.toString(rating),Toast.LENGTH_LONG).show();
//            }
//        });

        EventName.setText(event.getEventName());
        description.setText(event.getDescription());
        try {
            ownerName.setText(event.getOwner().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Eventlocal.setText(event.getLocation());
        int num = event.getAttendees().size();
        numAttend.setText(Integer.toString(num));

        Glide.with(this).load(event.getEventImage().getUrl()).into(EventPic);
//        Glide.with(this).load(event.getOwner().getProfgetUrl()).into(EventPic);
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
                    }else {
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

    public void joinEvent(Event event) {
        event.setAttendees(ParseUser.getCurrentUser());
        Log.d("EventDetailActivity", "joinEvent: " + event.getAttendees().size());
    }

    /*private void deepLinkIntoLyft() {
        if (isPackageInstalled(this, LYFT_PACKAGE)) {
            // TODO - set lyft destination here
            openLink(this, "lyft://");
            Log.d(TAG, "Lyft is already installed on your phone.");
        } else {
            openLink(this, String.format("https://www.lyft.com/signup/SDKSIGNUP?clientId=%s&sdkName=android_direct", CLIENT_ID));

            Log.d(TAG, "Lyft is not currently installed on your phone..");
        }
    }

    static void openLink(Activity activity, String link) {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
        playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        playStoreIntent.setData(Uri.parse(link));
        activity.startActivity(playStoreIntent);
    }

    static boolean isPackageInstalled(Context context, String packageId) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // ignored.
        }
        return false;
    }*/
}
