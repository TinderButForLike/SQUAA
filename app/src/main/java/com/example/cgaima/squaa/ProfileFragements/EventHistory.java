package com.example.cgaima.squaa.ProfileFragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.adapters.ProfileAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventHistory extends Fragment {
    @BindView(R.id.rvEventHistory) RecyclerView rvGrid;
    private ProfileAdapter mAdapter;
    private List<Event> events;


    private FragmentActivity listener;

    // Required empty public constructor
    public EventHistory() {}

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

        rvGrid = (RecyclerView) view.findViewById(R.id.rvEventHistory);

        // allows for optimizations
        rvGrid.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getActivity(), 2);

        rvGrid.setLayoutManager(layout);

        // get data
        events = new ArrayList<>();

        // Create an adapter
        mAdapter = new ProfileAdapter(events);

        // Bind adapter to list
        rvGrid.setAdapter(mAdapter);
        getPosts();

        return view;
    }
    public void getPosts(){
        final Event.Query query = new Event.Query();
        query.getTop().withOwner();
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e == null){
                    events.clear();
                    // mAdapter.addAll(objects);
                    for (int i = 0; i < objects.size(); i++){

                        events.add(objects.get(i));
                        mAdapter.notifyDataSetChanged();

                    }
                } else {
                    e.printStackTrace();
                    Log.e("EventHistory", "Failed to get Events");
                }
            }
        });
    }


}
