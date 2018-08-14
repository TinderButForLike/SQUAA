package com.example.cgaima.squaa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.ProfileFragements.AboutUser;
import com.example.cgaima.squaa.ProfileFragements.EventHistory;
import com.example.cgaima.squaa.ProfileFragements.Upcoming;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.activities.LoginActivity;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @BindView(R.id.ivProfilePic) ImageView profilePic;
    @BindView(R.id.avgRating) TextView avgRating;
    @BindView(R.id.rbUserRating) RatingBar rb;
    ParseUser currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentUser =  ParseUser.getCurrentUser();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // make search view show up for home fragment only
        inflater.inflate(R.menu.menu_fragment_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutText:
                ParseUser.logOut();
                final Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView tv = toolbar.findViewById(R.id.toolbar_title);
        tv.setText(currentUser.getUsername());
        View view = inflater.inflate(R.layout.fragment_profile, parent, false);
        ButterKnife.bind(this, view);
        // Defines the xml file for the fragment
        currentUser =  ParseUser.getCurrentUser();
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        currentUser =  ParseUser.getCurrentUser();
        float rating = (float) currentUser.getDouble("rating");
        avgRating.setText(String.valueOf(rating));
        rb.setRating(rating);
        Log.e("PROFILE FRAGMENT", "whoah i get created too wtf");
        try {
            Glide.with(this).load(currentUser.fetchIfNeeded().getParseFile("profile_picture").getUrl()).into(profilePic);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewPager = view.findViewById(R.id.vpContainer);
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new EventHistory(), "History");
        adapter.addFragment(new Upcoming(), "Upcoming");
        adapter.addFragment(new AboutUser(), "About");
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}