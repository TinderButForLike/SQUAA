package com.example.cgaima.squaa.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.cgaima.squaa.ProfileFragements.AboutUser;
import com.example.cgaima.squaa.ProfileFragements.EventHistory;
import com.example.cgaima.squaa.ProfileFragements.Logout;
import com.example.cgaima.squaa.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileLandingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_landing);

        toolbar = findViewById(R.id.toolbar);
        final ImageView imageInToolbar = toolbar.findViewById(R.id.ivProfilePic);
        ParseUser currentUser =  ParseUser.getCurrentUser();

//        imageInToolbar.setParseFile
//        Glide.with(this).load(event.getEventImage().getUrl()).into(EventPic);
//        Toast.makeText(this,email, Toast.LENGTH_LONG).show();

//        currentUser.getParseFile("profile_picture").getDataInBackground(new GetDataCallback() {
//            @Override
//            public void done(byte[] data, ParseException e) {
//                if (e == null) {
//                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    imageInToolbar.setImageBitmap(bmp);
//                }
//                else {
//                    Log.d("ProfileLandingActivity", "Can't load image");
//                    e.printStackTrace();
//                }
//            }
//        });


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.vpContainer);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventHistory(), "Event History");
        adapter.addFragment(new AboutUser(), "About User");
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