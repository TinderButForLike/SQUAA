package com.example.cgaima.squaa.activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.Chat;
import com.example.cgaima.squaa.fragments.CreateEventFragment;
import com.example.cgaima.squaa.fragments.HomeFragment;
import com.example.cgaima.squaa.fragments.MapFragment;
import com.example.cgaima.squaa.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.fragment_container) FrameLayout fragment_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // find the toolbar view inside layout and set tool as action bar for activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // remove default text
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final HomeFragment homeFragment = HomeFragment.newInstance("");
        final CreateEventFragment createEventFragment = new CreateEventFragment();
        final ProfileFragment profileFragment = new ProfileFragment();
        final MapFragment mapFragment = new MapFragment();
        //final Categories categories = new Categories();

        // initialize fragment manager
        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        // initialize bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    default:
                    case R.id.action_home:
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
                        getSupportActionBar().show();
                        return true;
                    case R.id.action_new_event:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, createEventFragment).commit();
                        getSupportActionBar().show();
                        return true;
                    case R.id.action_profile:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, profileFragment).commit();
                        //fragmentTransaction.replace(R.id.fragment_container, profileFragment).commit();
                        return true;
                    case R.id.action_map:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, mapFragment).commit();
                        return true;
                }
            }
        });

        if (getIntent().hasExtra("locationtext")){
            Log.d("Home Activity", "we have the goods.....");
            bottomNavigationView.setSelectedItemId(R.id.action_new_event);
        }
        // from event details activity to own profile
        else if (getIntent().hasExtra("event_chat")) {
            Fragment chatFragment = new Chat();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, chatFragment).commit();
            getSupportActionBar().hide();
       }
//        // from event details activity to event owner profile
//        else if (getIntent().hasExtra("eventOwner")) {
//            Fragment otherProfileFragment = OtherProfileFragment.newInstance(event);
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, otherProfileFragment).commit();
//        }
    }
    /*// inflate the menu, adds items to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment_home, menu);
        return true;
    }*/

}