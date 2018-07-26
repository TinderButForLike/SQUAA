package com.example.cgaima.squaa.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.cgaima.squaa.adapters.ParseDataSourceFactory;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.adapters.EventAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {
    @BindView(R.id.rvEvents) RecyclerView rvEvents;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private EventAdapter eventAdapter;
    private ArrayList<Event> events;
    // normally this data should be encapsulated in ViewModels, but shown here for simplicity
    LiveData<PagedList<Event>> posts;

    private FragmentActivity listener;

    // Required empty public constructor
    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder().setEnablePlaceholders(true)
                        .setPrefetchDistance(10)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        // initial page size to fetch can also be configured here too
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(20).build();

        ParseDataSourceFactory sourceFactory = new ParseDataSourceFactory();

        posts = new LivePagedListBuilder(sourceFactory, config).build();

        posts.observe(this, new Observer<PagedList<Event>>() {
            @Override
            public void onChanged(@Nullable PagedList<Event> tweets) {
                eventAdapter.submitList(tweets);
            }
        });

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("Home Fragment", "Home fragment created");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                //disable refresh during search
                swipeContainer.setEnabled(false);
                swipeContainer.setRefreshing(false);

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
                // enable after search is done
                swipeContainer.setEnabled(true);
                // reload all posts onto home
                loadTopPosts();
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        if (eventAdapter == null) {
            eventAdapter = new EventAdapter();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        // setup recycler view with adapter
        rvEvents.setLayoutManager(linearLayoutManager);
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

    /** loads top 20 events */
    // TODO - make infinite scrolling work
    private void loadTopPosts() {
        final Event.Query eventsQuery = new Event.Query();
        eventsQuery.getTop().withOwner();
        eventsQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e==null){
                    eventAdapter.addAll((ArrayList<Event>) objects);
                } else { e.printStackTrace(); }
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
        eventsQuery.containsWord("name", query);
        eventsQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e == null) {
                    Log.e("HomeActivity", String.valueOf(objects));
                    eventAdapter.addAll((ArrayList<Event>) objects);
                } else {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Search did not match any events", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
