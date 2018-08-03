package com.example.cgaima.squaa.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.ProfileFragements.AboutUser;
import com.example.cgaima.squaa.ProfileFragements.EventHistory;
import com.example.cgaima.squaa.ProfileFragements.Logout;
import com.example.cgaima.squaa.ProfileFragements.Upcoming;
import com.example.cgaima.squaa.R;
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
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, parent, false);
        ButterKnife.bind(this, view);
        // Defines the xml file for the fragment
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        ParseUser currentUser =  ParseUser.getCurrentUser();

        Log.e("PROFILE FRAGMENT", "whoah i get created too wtf");
        try {
            Glide.with(this).load(currentUser.fetchIfNeeded().getParseFile("profile_picture").getUrl()).into(profilePic);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewPager = (ViewPager) view.findViewById(R.id.vpContainer);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new EventHistory(), "History");
        adapter.addFragment(new Upcoming(), "Upcoming");
        adapter.addFragment(new AboutUser(), "About");
        adapter.addFragment(new Logout(), "Logout");
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