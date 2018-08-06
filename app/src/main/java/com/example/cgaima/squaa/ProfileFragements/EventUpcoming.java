package com.example.cgaima.squaa.ProfileFragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.adapters.UpcomingAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventUpcoming extends Fragment {
    @BindView(R.id.rvEventHistory) RecyclerView rvGrid;
    private UpcomingAdapter mAdapter;
    private List<Event> events;


    private FragmentActivity listener;

    // Required empty public constructor
    public EventUpcoming() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("EventHistory", "Event history fragment created");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_history, container, false);
        ButterKnife.bind(this, view);



        return view;
    }



}
