package com.example.cgaima.squaa;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cgaima.squaa.Models.Event;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private EventAdapter eventAdapter;
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // setup recycler view with adapter
        events = new ArrayList<>();
        eventAdapter = new EventAdapter(events);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(eventAdapter);

        // setup container view for refresh function
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopEvents();
                swipeContainer.setRefreshing(false);
            }
        });

        loadTopEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // perform query
                        fetchQueryEvents(query);
                        // avoid issues with firing twice
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }

                });
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                loadTopEvents();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
            case R.id.miNewEvent:
                //If create new event icon selected, launch event activity
                Intent intent = new Intent(this,EventActivity.class);
                startActivity(intent);
                //finish();

            /*If you wish to open new activity and close this one
            startNewActivity();
            */
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** loads top 20 events */
    // TODO - make infinite scrolling work?
    private void loadTopEvents() {
        eventAdapter.clear();
        final Event.Query eventsQuery = new Event.Query();
        eventsQuery.getTop();
        eventsQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e==null){
//                    Log.e("HomeActivity","objects size " + objects.size());
                    eventAdapter.setItems(objects);
                }
                else { e.printStackTrace(); }
            }
        });
    }

    /** fetch event by name
     * now case sensitive and searches for all that contains the search query word.
     * cannot go back to main screen after search */
    private void fetchQueryEvents(String query) {
        // clear adapter
        eventAdapter.clear();
        // create a new query
        final Event.Query eventsQuery = new Event.Query();
        eventsQuery.containsWord(query);
        eventsQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e == null) {
                    Log.e("HomeActivity", String.valueOf(objects));
                    eventAdapter.setItems(objects);
                } else {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Search did not match any events", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
