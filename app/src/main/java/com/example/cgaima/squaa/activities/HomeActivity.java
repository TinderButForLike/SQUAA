package com.example.cgaima.squaa.activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.CreateEventFragment;
import com.example.cgaima.squaa.fragments.HomeFragment;
import com.example.cgaima.squaa.fragments.MapFragment;
import com.example.cgaima.squaa.fragments.OtherProfileFragment;
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

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final HomeFragment homeFragment = new HomeFragment();
        final CreateEventFragment createEventFragment = new CreateEventFragment();
        final ProfileFragment profileFragment = new ProfileFragment();
        final MapFragment mapFragment = new MapFragment();

        // initialize fragment manager
        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        // initialize bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    default:
                    case R.id.action_home:
                        //supportFinishAfterTransition();
                        //viewPager.setCurrentItem(0);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
                        return true;
                    case R.id.action_new_event:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, createEventFragment).commit();
                        return true;
                    case R.id.action_profile:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, profileFragment).commit();
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
    }

    // inflate the menu, adds items to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}