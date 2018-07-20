package com.example.cgaima.squaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity {

    //resource variables

    @BindView(R.id.ivEventPic)
    ImageView EventPic;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this); //bind butterknife after
        Parcelable parcel = this.getIntent().getParcelableExtra("event");
        final Event event = (Event) Parcels.unwrap(parcel);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                joinEvent(event);
                numAttend.setText(Integer.toString(event.getAttendees().size()));
            }
        });

//        rb =(RatingBar)findViewById(R.id.ratingBar1);
//
//        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
//
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating,
//                                        boolean fromUser) {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(),Float.toString(rating),Toast.LENGTH_LONG).show();
//
//            }
//
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
                Intent i = new Intent(EventDetailActivity.this, HomeActivity.class);
                i.putExtra("event_owner", Parcels.wrap(event));
                startActivity(i);
            }
        });

    }

    public void joinEvent(Event event){
        event.setAttendees(ParseUser.getCurrentUser());
        Log.d("EventDetailActivity", "joinEvent: " + event.getAttendees().size());

    }
}
