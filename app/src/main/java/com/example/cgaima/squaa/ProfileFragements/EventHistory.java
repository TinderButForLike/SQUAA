package com.example.cgaima.squaa.ProfileFragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.profileAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class EventHistory extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private RecyclerView rvGrid;
    private profileAdapter mAdapter;
    private List<Event> events;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_event_history, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        // Find RecyclerView and bind to adapter
        rvGrid = (RecyclerView) view.findViewById(R.id.rvEventHistory);

        // allows for optimizations
        rvGrid.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getActivity(), 2);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvGrid.setLayoutManager(layout);

        // get data
        events = new ArrayList<>();

        // Create an adapter
        mAdapter = new profileAdapter(events);

        // Bind adapter to list
        rvGrid.setAdapter(mAdapter);
        getPosts();
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
