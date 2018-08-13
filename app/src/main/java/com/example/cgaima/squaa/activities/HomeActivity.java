package com.example.cgaima.squaa.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.cgaima.squaa.MyFirebaseInstanceIDService;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.CreateEventFragment;
import com.example.cgaima.squaa.fragments.HomeFragment;
import com.example.cgaima.squaa.fragments.MapFragment;
import com.example.cgaima.squaa.fragments.ProfileFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.fragment_container) FrameLayout fragment_container;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    //check to make sure the device has the google play services sdk. if not,
    //display a dialog. that allows them to enable it
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("HomeActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if(checkPlayServices()) {
            Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
            startService(intent);
        }

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
        if (getIntent().hasExtra("year")){
            Log.d("Home Activity", "we have the year.....");
            bottomNavigationView.setSelectedItemId(R.id.action_new_event);
        }
        if (getIntent().hasExtra("month")){
            Log.d("Home Activity", "we have the month.....");
            bottomNavigationView.setSelectedItemId(R.id.action_new_event);
        }
        if (getIntent().hasExtra("date")) {
            Log.d("Home Activity", "we have the date");
            bottomNavigationView.setSelectedItemId(R.id.action_new_event);
        }

//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String newToken = instanceIdResult.getToken();
//                Log.e("newToken",newToken);
//            }
//        });


    }




}