package com.example.cgaima.squaa.activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.CreateEventFragment;
import com.example.cgaima.squaa.fragments.HomeFragment;
import com.example.cgaima.squaa.fragments.MapFragment;
import com.example.cgaima.squaa.fragments.OtherProfileFragment;
import com.example.cgaima.squaa.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    public HomeFragment homeFragment;
    public CreateEventFragment eventFragment;
    public ProfileFragment profileFragment;
    public OtherProfileFragment otherProfileFragment;
    public MapFragment mapFragment;
    public int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        // find the toolbar view inside layout and set tool as action bar for activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // remove default text

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        if (eventFragment == null) {
            eventFragment = new CreateEventFragment();
        }
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }

        if (otherProfileFragment == null) {
            otherProfileFragment = new OtherProfileFragment();
        }
        if(mapFragment == null) {
            mapFragment = new MapFragment();
        }
        state = 0;

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {


            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    default:
                    case 0:
                        return homeFragment;
                    case 1:
                        return eventFragment;
                    case 2:
                        getSupportActionBar().hide();
                        return profileFragment;
                    case 3:
                        getSupportActionBar().hide();
                        return otherProfileFragment;
                    case 4:
                        getSupportActionBar().hide();
                        return mapFragment;
                }
            }
            @Override
            public int getCount() { return 5; }
        };

        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if(getIntent().hasExtra("event_owner")){

            Toast.makeText(this,"hey",Toast.LENGTH_LONG);
            viewPager.setCurrentItem(3);

        } else if (getIntent().hasExtra("locationtext")){
            Log.d("Home Activity", "we have the goods.....");
            bottomNavigationView.setSelectedItemId(R.id.action_new_event);
            //viewPager.setCurrentItem(1);
        } else if (getIntent().hasExtra("profile")){
            bottomNavigationView.setSelectedItemId(R.id.action_profile);
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            default:
            case R.id.action_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.action_new_event:
                viewPager.setCurrentItem(1);
                break;
            case R.id.action_profile:
                viewPager.setCurrentItem(2);
                break;
            case R.id.action_map:
                viewPager.setCurrentItem(4);
                break;
        }
        return true;
    }
}
