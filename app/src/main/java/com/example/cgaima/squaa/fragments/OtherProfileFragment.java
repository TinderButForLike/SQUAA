package com.example.cgaima.squaa.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.ProfileFragements.AboutUser;
import com.example.cgaima.squaa.ProfileFragements.EventHistory;
import com.example.cgaima.squaa.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OtherProfileFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_other_profile, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final ImageView imageInToolbar = (ImageView) toolbar.findViewById(R.id.ivProfilePic);
        Parcelable parcel = getActivity().getIntent().getParcelableExtra("event_owner");
        if (parcel != null) {
            final Event event = (Event) Parcels.unwrap(parcel);
            ParseUser owner = event.getOwner();
            try {
                Glide.with(this).load(owner.fetchIfNeeded().getParseFile("profile_picture").getUrl()).into(imageInToolbar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) view.findViewById(R.id.vpContainer);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());
        adapter.addFragment(new EventHistory(), "Event History");
        adapter.addFragment(new AboutUser(), "About");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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