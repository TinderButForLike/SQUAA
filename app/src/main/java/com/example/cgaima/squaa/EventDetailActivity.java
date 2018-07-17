package com.example.cgaima.squaa;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.parse.ParseException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this); //bind butterknife after

        Parcelable parcel = this.getIntent().getParcelableExtra("event");
        Event event = (Event) Parcels.unwrap(parcel);
        EventName.setText(event.getEventName());
        description.setText(event.getDescription());
        try {
            ownerName.setText(event.getOwner().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Eventlocal.setText(event.getLocation());

        Glide.with(this).load(event.getEventImage().getUrl()).into(EventPic);
//        Glide.with(this).load(event.getOwner().getProfgetUrl()).into(EventPic);

    }
}
