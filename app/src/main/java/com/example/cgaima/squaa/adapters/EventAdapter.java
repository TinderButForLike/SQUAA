package com.example.cgaima.squaa.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.activities.EventDetailActivity;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    Context context;
    List<Event> events;
    private final int REQUEST_CODE = 21;

    //resource variables
    boolean joined;

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
        holder.event_name.setText(event.getEventName());
        holder.supporting_text.setText(event.getDescription());
        holder.location.setText(event.getLocation());
        holder.date.setText(event.getDate());

        // TODO - set correct variables to UI
        //holder.tvAttendees.setText(event.getString("attendees"));
        //holder.tvDate.setText(event.getDate());
        //holder.tvLocation.setText(event.getLocation());

        if (event.getEventImage()==null) {
            Glide.with(context).load(
                    "https://www.google.com/url?sa=i&source=images&cd=&ved=" +
                            "2ahUKEwiKgcfT56TcAhWzHDQIHZ7ICZgQjRx6BAgBEAU&url=http%3A%2F%2F" +
                            "www.washingtonpost.com%2Frecipes%2Flunch-box-pasta-salad%2F15483%" +
                            "2F&psig=AOvVaw3QDPftuCv2CSZjHVzwoXZB&ust=1531871357428835")
                    .into(holder.media_image);
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

        holder.media_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EventDetailActivity.class);
                i.putExtra("event", Parcels.wrap(event));
                //i.putExtra("post", post);
                ((Activity) context).startActivityForResult(i, REQUEST_CODE);
            }
        });
        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joined = false;
                if (!joined){
                    joinEvent(event);
                    holder.numAttend.setText(Integer.toString(event.getAttendees().size()));
                    joined = true;
                    //fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_unjoin));
                }
                else{
                    //fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_join));
                    joined = false;
                    ParseUser current = ParseUser.getCurrentUser();
                    event.removeAll("attendees", Collections.singleton(current));
                    try {
                        event.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.numAttend.setText(Integer.toString(event.getAttendees().size()));
                }
            }
        });

        try {
            holder.tvOwner.setText(event.getOwner().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int num = event.getAttendees().size();
        holder.numAttend.setText(Integer.toString(num));

        try {
            Glide.with(context).load(event.getOwner().fetchIfNeeded()
                    .getParseFile("profile_picture").getUrl()).into(holder.ownerPic);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void joinEvent(Event event){
        event.setAttendees(ParseUser.getCurrentUser());
        Log.d("EventDetailActivity", "joinEvent: " + event.getAttendees().size());
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.media_image) ImageView media_image;
        @BindView(R.id.primary_text) TextView event_name;
        @BindView(R.id.supporting_text) TextView supporting_text;
        @BindView(R.id.sub_text) TextView date;
        @BindView(R.id.sub_text2) TextView location;
        @BindViews({R.id.supporting_text, R.id.tvOwner, R.id.ivOwnerPic, R.id.action_button_1,
                R.id.tvNumAttend}) List<View> moreView;
        @BindView(R.id.expand_button) ImageButton expandButton;
        @BindView(R.id.action_button_1) Button join;
        @BindView(R.id.tvNumAttend) TextView numAttend;
        @BindView(R.id.tvOwner) TextView tvOwner;
        @BindView(R.id.ivOwnerPic) ImageView ownerPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.expand_button)
        public void onExpand() {
            if (supporting_text.getVisibility() == View.VISIBLE) {
                expandButton.setImageResource(R.drawable.ic_expand_more_black_36dp);
                ButterKnife.apply(moreView, VISIBILITY, View.GONE);
            }
            else {
                expandButton.setImageResource(R.drawable.ic_expand_less_black_36dp);
                ButterKnife.apply(moreView, VISIBILITY, View.VISIBLE);
            }
        }
    }

    //custom setter
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
}
