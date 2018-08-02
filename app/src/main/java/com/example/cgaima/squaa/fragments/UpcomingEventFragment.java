package com.example.cgaima.squaa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingEventFragment extends Fragment {
    //resource variables
    boolean joined;
    private static final String TAG = "lyft:Example";
    private static final String CLIENT_ID = "rzupE13z8Yo2";
    private static final String LYFT_PACKAGE = "me.lyft.android";
    FloatingActionButton fab;
    Event event;

    @BindView(R.id.ivEventPic) ImageView eventPic;
    @BindView(R.id.tvEventName) TextView eventName;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.tvDescription) TextView description;
    @BindView(R.id.numAttending) TextView numAttending;
    @BindView(R.id.tvOwner) TextView ownerName;
    @BindView(R.id.ivOwnerPic) ImageView ownerPic;
    @BindView(R.id.tvLocation) TextView eventLocation;
    @BindView(R.id.ratingBar) RatingBar rb;

    // required empty
    public UpcomingEventFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_event, container, false);
        ButterKnife.bind(this, view);
        // set ui

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
