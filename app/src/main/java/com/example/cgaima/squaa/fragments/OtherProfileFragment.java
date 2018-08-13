package com.example.cgaima.squaa.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;


public class OtherProfileFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentActivity listener;
    private Boolean added;


    // Required empty public constructor
    public OtherProfileFragment() { }

    public static OtherProfileFragment newInstance(Event event) {
        OtherProfileFragment otherProfileFragment = new OtherProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        otherProfileFragment.setArguments(args);
        return otherProfileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("OtherProfileFragment", "OtherProfile fragment created");
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);
        ButterKnife.bind(this, view);

        toolbar = view.findViewById(R.id.toolbar);
        final ImageView imageInToolbar = toolbar.findViewById(R.id.ivProfilePic);

        Event event = getArguments().getParcelable("event");

        final ParseUser owner = event.getOwner();

        try {
            Glide.with(this).load(owner.fetchIfNeeded().getParseFile("profile_picture").getUrl()).into(imageInToolbar);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("OtherProfileFragment", "I GET OPENED");
        final FloatingActionButton fab = view.findViewById(R.id.fabFriend);
        added = false;
        fab.setImageDrawable(ContextCompat.getDrawable(((AppCompatActivity) getActivity()).getApplicationContext(), R.drawable.ic_addfriend));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!added) {
                    ParseUser current = ParseUser.getCurrentUser();
                    current.addUnique("friends", owner);
                    try {
                        current.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fab.setImageDrawable(ContextCompat.getDrawable(((AppCompatActivity) getActivity()).getApplicationContext(), R.drawable.ic_unfriend));
                    added = true;
                } else {
                    ParseUser current = ParseUser.getCurrentUser();
                    current.removeAll("friends", Collections.singleton(owner));
                    try {
                        current.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    fab.setImageDrawable(ContextCompat.getDrawable(((AppCompatActivity) getActivity()).getApplicationContext(), R.drawable.ic_addfriend));
                    added = false;
                }
            }
        });


        viewPager = (ViewPager) view.findViewById(R.id.vpContainer);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
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