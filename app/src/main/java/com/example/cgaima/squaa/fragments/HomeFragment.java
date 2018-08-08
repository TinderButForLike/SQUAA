package com.example.cgaima.squaa.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.adapters.EventAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {
    @BindView(R.id.rvEvents) RecyclerView rvEvents;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private EventAdapter eventAdapter;
    private ArrayList<Event> events;

    private FragmentActivity listener;
    SearchView searchView;

    // Required empty public constructor
    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("Home Fragment", "Home fragment created");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // make search view show up for home fragment only
        inflater.inflate(R.menu.menu_fragment_home, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView = (SearchView) searchItem.getActionView();

        /*SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));*/


       /*searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                loadTopPosts();
                // enable refresh after menu closed
                swipeContainer.setEnabled(true);
                swipeContainer.setRefreshing(true);
                searchItem.collapseActionView();
                return true;
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return false;
            case R.id.menu_current:
                Toast.makeText(getContext(), "Current events!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_location:
                fetchNearEvents();
                //Toast.makeText(getContext(), "Location!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // disable refresh during search view
                swipeContainer.setEnabled(false);
                swipeContainer.setRefreshing(false);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        if (eventAdapter == null) {
            eventAdapter = new EventAdapter(new ArrayList<Event>());
        }

        // setup recycler view with adapter
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.setAdapter(eventAdapter);

        // setup container view for refresh function
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventAdapter.clear();
                loadTopPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        loadTopPosts();

        return view;
    }

    // TODO - make infinite scrolling work with query
    // load top 20 events
    private void loadTopPosts() {
        final Event.Query eventsQuery = new Event.Query();
        eventsQuery.getTop().withOwner();
        eventsQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e==null){
                    eventAdapter.setItems(objects);
                } else { e.printStackTrace(); }
            }
        });
    }

    // TODO - make this not case sensitive and search by more fields
    // fetch event by name
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
                    Toast.makeText(getContext(), "Search did not match any events", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void fetchNearEvents() {
        // clear adapter
        eventAdapter.clear();

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        // check location permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Turn on location permissions to see near events!", Toast.LENGTH_SHORT).show();
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ParseGeoPoint parseLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            // create new query
            final Event.Query eventsQuery = new Event.Query();
            eventsQuery.getTopNear(parseLocation);
            eventsQuery.findInBackground(new FindCallback<Event>() {
                @Override
                public void done(List<Event> objects, ParseException e) {
                    if (e == null) {
                        Log.e("HomeFragment", String.valueOf(objects));
                        eventAdapter.setItems(objects);
                        if (objects.isEmpty()) {
                            Toast.makeText(getContext(), "There are no events near you", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Events near you is not currently available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
