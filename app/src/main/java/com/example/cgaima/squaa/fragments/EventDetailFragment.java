package com.example.cgaima.squaa.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.Models.GlideApp;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.activities.HomeActivity;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailFragment extends Fragment {

    //resource variables
    boolean joined;
    private static final String CLIENT_ID = "rzupE13z8Yo2";
    private static final String CLIENT_TOKEN = "4IA9raWjUI3rr3igs0SNcIEzrvWmUhl8EAZGaajBtVeEGrg7CBj+tzqmri6pDEP2yC3QN/D/23/Bc6Ew0DEX5IfLXfJv0bZt2JYBNIf1aNeXazxXU8T32NU=";

    Event event;
    EventAttendance eventAttendance;

    @BindView(R.id.ivEventPic)
    ImageView eventPic;
    @BindView(R.id.tvEventName)
    TextView eventName;
    @BindView(R.id.tvDate)
    TextView date;
    @BindView(R.id.tvDescription)
    TextView description;
    @BindView(R.id.tvNumAttend)
    TextView numAttend;
    @BindView(R.id.tvOwner)
    TextView ownerName;
    @BindView(R.id.ivOwnerPic)
    ImageView ownerPic;
    @BindView(R.id.tvLocation)
    TextView location;
    @BindView(R.id.ratingBar)
    RatingBar rb;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.lyft_button)
    LyftButton lyftButton;
    @BindView(R.id.join)
    Button join;
    @BindView(R.id.ivChat)
    ImageView ChatIcon;
    ActionBar actionBar;

    public EventDetailFragment() {
    }

    public static EventDetailFragment newInstance(Event event, EventAttendance eventAttendance) {
        EventDetailFragment eventDetailActivity = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putParcelable("eventAttendance", eventAttendance);
        eventDetailActivity.setArguments(args);

        return eventDetailActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        // get arguments
        event = getArguments().getParcelable("event");
        eventAttendance = getArguments().getParcelable("eventAttendance");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_event_detail, container, false);
        ButterKnife.bind(this, view);

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date tmrw = cal.getTime();
        Date fromDate = event.getDate("fromDate");
        Date toDate = event.getDate("toDate");

        // if future event - allow user to join_button / unjoin and call event
        if (fromDate.after(today)) {
            // set join_button event initial UI
            join.setVisibility(View.VISIBLE);
            joined = EventAttendance.isAttending(event);
            if (joined) { toggleJoinButton(false); }
            else { toggleJoinButton(true); }
        }

        // if current event - allow user to call uber
        else if (fromDate.before(today) && toDate.after(today)) {
            ApiConfig apiConfig = new ApiConfig.Builder()
                    .setClientId(CLIENT_ID)
                    .setClientToken(CLIENT_TOKEN)
                    .build();

            lyftButton.setApiConfig(apiConfig);
            LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            // check location permission
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Turn on permissions to call Lyft from our app!", Toast.LENGTH_SHORT).show();
                lyftButton.setVisibility(View.GONE); // hide lyft button
            } else {
                lyftButton.setVisibility(View.VISIBLE);
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
        else if (toDate.before(today) && toDate.before(tmrw) && eventAttendance != null) {
            if (eventAttendance.getBoolean("rated")) {
                Toast.makeText(getContext(), "You've already rated this event!", Toast.LENGTH_SHORT).show();
            } else {
                tvRate.setVisibility(View.VISIBLE);
                tvRate.setText("rate this event");
                rb.setVisibility(View.VISIBLE);
                rb.setIsIndicator(false);
                // set rating bar
                rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, final float rating,
                                                boolean fromUser) {
                        int numRatings = event.getInt("numRatings");
                        double avgRating = (numRatings * event.getDouble("rating") + rating) / (numRatings + 1);
                        event.put("rating", avgRating);
                        event.put("numRatings", numRatings + 1);
                        eventAttendance.put("rated", true);
                        eventAttendance.saveInBackground();
                        event.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getContext(), String.format("Successfully rated event %.1f stars", rating), Toast.LENGTH_LONG).show();
                                    tvRate.setVisibility(View.GONE);
                                    rb.setVisibility(View.GONE);
                                } else {
                                    Log.e("EventDetailFragment", e.toString());
                                    Toast.makeText(getContext(), "Failed to rate the event try again later", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        }

        // if past event and user didn't attend - show only event details
        else if (toDate.after(today) && toDate.before(tmrw) && eventAttendance == null) {
            Toast.makeText(getContext(), "This event has already past and rating is not available yet. Check back in one day!", Toast.LENGTH_LONG).show();
        }

        // if past event after one day
        else {
            tvRate.setVisibility(View.VISIBLE);
            rb.setVisibility(View.VISIBLE);
            rb.setIsIndicator(true);
            rb.setStepSize((float) 0.5);

            int rating = event.getInt("rating");
            rb.setRating(rating);
            tvRate.setText(String.valueOf(rating));

            Toast.makeText(getContext(), "This event has already past! Rating has closed.", Toast.LENGTH_LONG).show();
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

        try {
            Glide.with(this).load(event.getOwner().fetchIfNeeded().getParseFile("profile_picture").getUrl()).into(ownerPic);
            if (event.getEventImage() == null) {
                eventPic.setImageResource(R.drawable.image_default);
            } else {
                GlideApp.with(this)
                        .load(event.getEventImage().getUrl())
                        .listener(new RequestListener<Drawable>() { // for smooth shared element transition
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                startPostponedEnterTransition();
                                return false;
                            }
                        })
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
                    // if current user
                    if (event.getOwner().fetchIfNeeded().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        Fragment otherProfileFragment = new ProfileFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, otherProfileFragment).commit();
                    } else {
                        Fragment otherProfileFragment = OtherProfileFragment.newInstance(event);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, otherProfileFragment).commit();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // TODO - chat icon
        ChatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment chat = new Chat();
                Bundle args = new Bundle();
                args.putParcelable("event", event);
                chat.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, chat).commit();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView tv = toolbar.findViewById(R.id.toolbar_title);
        tv.setText("");
        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @OnClick(R.id.join)
    public void onJoin() {
        final boolean joined = EventAttendance.isAttending(event);
        // join_button event if not already joined
        if (!joined) {
            final EventAttendance newEventAttendance = new EventAttendance();
            newEventAttendance.setEventAttendance(ParseUser.getCurrentUser(), event);
            newEventAttendance.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        toggleJoinButton(false);
                        Log.d("EventAdapter", "Successfully joined event. :) ");
                    } else {
                        Toast.makeText(getContext(), "Failed to join_button event", Toast.LENGTH_LONG).show();
                        Log.e("EventAdapter", e.toString());
                    }
                }
            });
        }
        // unjoin event if already joined
        else {
            toggleJoinButton(true);
            /*EventAttendance.Query query = new EventAttendance.Query();
            query.findEventAttendance(ParseUser.getCurrentUser(), event);
            try {
                toggleJoinButton(true);
                query.getFirst().deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            toggleJoinButton(true);
                            Log.d("EventDetailFragment", "Successfully unjoined event. ");
                        }
                        else {
                            Toast.makeText(getContext(),"Failed to unjoin event", Toast.LENGTH_LONG).show();
                            Log.e("EventDetailFragment", e.toString());
                        }
                    }
                });
                Log.d("EventDetailFragment", "Successfully unjoined event.");
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
    }
    public void toggleJoinButton(boolean toggle) {
        numAttend.setText(String.valueOf(EventAttendance.getNumAttending(event)));
        if (toggle) {
            join.setText("join");
            join.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.secondaryColor, getActivity().getTheme())));
        }
        else {
            join.setText("joined.");
            join.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray, getActivity().getTheme())));
            join.setEnabled(false);
        }
    }
}