package com.example.cgaima.squaa.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.Models.GlideApp;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.EventDetailFragment;
import com.example.cgaima.squaa.fragments.OtherProfileFragment;
import com.example.cgaima.squaa.fragments.ProfileFragment;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        EventAttendance eventAttendance = null;
        try {
            eventAttendance = (EventAttendance) new EventAttendance.Query().findEventAttendance(ParseUser.getCurrentUser(), event).getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // set event name, description, location, date
        holder.event_name.setText(event.getEventName());
        holder.supporting_text.setText(event.getDescription());
        holder.location.setText(event.getLocation());


        Date fromDate = event.getDate("fromDate");
        Date toDate = event.getDate("toDate");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String dateString = String.format("%s - %s", simpleDateFormat.format(fromDate), simpleDateFormat.format(toDate));
        holder.date.setText(dateString);

        // set owner name, profile picture, media image
        try {
            holder.tvOwner.setText(event.getOwner().fetchIfNeeded().getUsername());
            GlideApp.with(context).load(event.getOwner().fetchIfNeeded()
                    .getParseFile("profile_picture").getUrl()).into(holder.ownerPic);
            if (event.getEventImage() == null) {
                holder.media_image.setImageResource(R.drawable.image_default);
            }
            else {
                GlideApp.with(context)
                        .load(event.getEventImage().getUrl())
                        .error(R.drawable.image_default)
                        .into(holder.media_image);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // set button and numAttended initial UI
        holder.numAttend.setText(String.valueOf(EventAttendance.getNumAttending(event)));
        final boolean joined = EventAttendance.isAttending(event);
        if (joined) {
            holder.join.setText("unjoin?");
            holder.join.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.light_gray, context.getTheme())));
        }

        // after current user clicks join
        final EventAttendance finalEventAttendance = eventAttendance;
        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean joined = EventAttendance.isAttending(event);
                // join event if not already joined
                if (!joined) {
                    final EventAttendance newEventAttendance = new EventAttendance();
                    newEventAttendance.setEventAttendance(ParseUser.getCurrentUser(), event);
                    newEventAttendance.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                holder.join.setText("unjoin?");
                                holder.join.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.light_gray, context.getTheme())));
                                holder.numAttend.setText(String.valueOf(EventAttendance.getNumAttending(event)));
                                Log.d("EventAdapter", "Successfully joined event. :) ");
                            } else {
                                Toast.makeText(context,"Failed to join event", Toast.LENGTH_LONG).show();
                                Log.e("EventAdapter", e.toString());
                            }
                        }
                    });
                    // TODO - put check mark on media image and make button gray or remove from home screen
                }
                // unjoin event if already joined
                else{
                    finalEventAttendance.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                holder.join.setText("join");
                                holder.join.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.secondaryColor, context.getTheme())));
                                holder.numAttend.setText(String.valueOf(EventAttendance.getNumAttending(event)));
                                Log.d("EventAdapter", "Successfully unjoined event. ");
                            }
                            else {
                                Toast.makeText(context,"Failed to unjoin event", Toast.LENGTH_LONG).show();
                                Log.e("EventAdapter", e.toString());
                            }
                        }
                    });
                }
            }
        });

        // launch other profile fragment
        holder.ownerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // if current user
                    if (event.getOwner().fetchIfNeeded().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                        Fragment otherProfileFragment = new ProfileFragment();
                        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, otherProfileFragment).commit();
                    } else {
                        Fragment otherProfileFragment = OtherProfileFragment.newInstance(event);
                        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, otherProfileFragment).commit();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // launch event details view
        final EventAttendance finalEventAttendance1 = eventAttendance;
        holder.media_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment eventDetailActivity = EventDetailFragment.newInstance(event, finalEventAttendance1);

                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addSharedElement(holder.media_image, "eventCard")
                        .replace(R.id.fragment_container, eventDetailActivity)
                        .addToBackStack(null);

                fragmentTransaction.commit();
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

}