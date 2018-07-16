package com.example.cgaima.squaa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    // TODO - change eventFragment and profileFragment
    public HomeFragment homeFragment;
    public HomeFragment eventFragment;
    public HomeFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        // TODO - change eventFragment and profileFragment
        if (eventFragment == null) {
            eventFragment = new HomeFragment();
        }
        if (profileFragment == null) {
            profileFragment = new HomeFragment();
        }

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
                        return profileFragment;
                }
            }
            @Override
            public int getCount() { return 3; }
        };

        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
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
        }
        return true;
    }

}
