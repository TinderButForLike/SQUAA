package com.example.cgaima.squaa.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.activities.EventDetailActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.example.cgaima.squaa.activities.HomeActivity;
import com.example.cgaima.squaa.fragments.OtherProfileFragment;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

// TODO - rename variables so that they are consistent
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    Context context;
    List<Event> events;
    private final int REQUEST_CODE = 21;
    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Event event = events.get(position);

        // set event name, description, location, date
        holder.event_name.setText(event.getEventName());
        holder.supporting_text.setText(event.getDescription());
        holder.location.setText(event.getLocation());
        holder.date.setText(event.getDate());

        // set media image
        if (event.getEventImage()==null) {
            holder.media_image.setImageResource(R.drawable.image_default);
        } else {
            event.getEventImage().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.media_image.setImageBitmap(bmp);
                    }
                    else {
                        Log.d("EventAdapter", "Can't load image");
                        e.printStackTrace();
                    }
                }
            });
        }

        // set owner name
        try {
            holder.tvOwner.setText(event.getOwner().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // set owner profile picture
        try {
            Glide.with(context).load(event.getOwner().fetchIfNeeded()
                    .getParseFile("profile_picture").getUrl()).into(holder.ownerPic);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // set button and numAttended initial UI
        holder.numAttend.setText(String.valueOf(getNumAttending(event)));
        final boolean joined = isAttending(event);
        if (joined) { holder.join.setText("unjoin?"); }

        // after current user clicks join
        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean joined = isAttending(event);
                if (!joined){
                    final EventAttendance newEventAttendance = new EventAttendance();
                    newEventAttendance.setEventAttendance(ParseUser.getCurrentUser(), event);
                    newEventAttendance.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                holder.join.setText("unjoin?");
                                holder.numAttend.setText(String.valueOf(getNumAttending(event)));
                                Log.d("EventAdapter", "Successfully joined event. :) ");
                            } else {
                                Toast.makeText(context,"Failed to join event", Toast.LENGTH_LONG).show();
                                Log.e("EventAdapter", e.toString());
                            }
                        }
                    });
                    // TODO - put check mark on media image and make button gray or remove from home screen
                }
                else{
                    EventAttendance.Query query = new EventAttendance.Query();
                    query.findEventAttendance(ParseUser.getCurrentUser(), event);
                    try {
                        query.getFirst().deleteInBackground();
                        holder.join.setText("join");
                        holder.numAttend.setText(String.valueOf(getNumAttending(event)));
                        Log.d("EventAdapter", "Successfully unjoined event. ");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // TODO - launch other profile fragment
        holder.ownerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment otherProfileFragment = new OtherProfileFragment();
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, otherProfileFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TODO - rename these variables
        @BindView(R.id.media_image) ImageView media_image;
        @BindView(R.id.primary_text) TextView event_name;
        @BindView(R.id.supporting_text) TextView supporting_text;
        @BindView(R.id.sub_text) TextView date;
        @BindView(R.id.sub_text2) TextView location;
        @BindView(R.id.expand_button) ImageButton expandButton;
        @BindView(R.id.action_button_1) Button join;
        @BindView(R.id.tvNumAttend) TextView numAttend;
        @BindView(R.id.tvOwner) TextView tvOwner;
        @BindView(R.id.ivOwnerPic) ImageView ownerPic;
        @BindViews({R.id.action_button_1, R.id.tvNumAttend}) List<View> joinView;
        @BindViews({R.id.supporting_text, R.id.tvOwner, R.id.ivOwnerPic, R.id.action_button_1,
                R.id.tvNumAttend, R.id.owner, R.id.attending}) List<View> expandView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.expand_button)
        public void onExpand() {
            if (supporting_text.getVisibility() == View.VISIBLE) {
                expandButton.setImageResource(R.drawable.ic_expand_more_black_36dp);
                ButterKnife.apply(expandView, VISIBILITY, View.GONE);
            }
            else {
                expandButton.setImageResource(R.drawable.ic_expand_less_black_36dp);
                ButterKnife.apply(expandView, VISIBILITY, View.VISIBLE);
            }
        }
    }

    // custom setter to toggle expand view
    public static final ButterKnife.Setter<View, Integer> VISIBILITY = new ButterKnife.Setter<View, Integer>() {
        @Override
        public void set(@NonNull View view, Integer value, int index) {
            view.setVisibility(value);
        }
    };

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    // check if current user is attending event
    public boolean isAttending(Event event) {
        EventAttendance.Query query = new EventAttendance.Query();
        query.findEventAttendance(ParseUser.getCurrentUser(), event);
        try {
            List eventAttendance = query.find();
            return !eventAttendance.isEmpty();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // defaults to not attending
        return false;
    }


    // get total number of attendees for event
    public int getNumAttending(Event event) {
        // defaults num attending to 0
        int numAttending = 0;
        EventAttendance.Query query = new EventAttendance.Query();
        query.findAllEventAttendance(event);
        try {
            List eventAttendance = query.find();
            numAttending = eventAttendance.size();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numAttending;
    }
}