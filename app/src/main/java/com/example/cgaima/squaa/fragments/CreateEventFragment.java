package com.example.cgaima.squaa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateEventFragment extends Fragment {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.location)
    EditText location;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.privacy)
    EditText privacy;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.create)
    Button create;

    // Required empty public constructor
    public CreateEventFragment() { }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.create)
    public void onCreate(View view) {
        String mName = name.getText().toString();
        String mLocation = location.getText().toString();
        //Date mDate = (Date) date.getText();
        String mDescription = description.getText().toString();

        createEvent(mName, mLocation, mDescription);//mDate, mDescription);

        // TODO - set fragment to home fragment after creating event
        /*Fragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.viewpager, homeFragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();*/
    }

    //create a new event
    private void createEvent(String name, String location, String description) { //TODO add privacy, image, date
        final Event newEvent = new Event();
        newEvent.setEventName(name);
        newEvent.setLocation(location);
        //newEvent.setDate(date);
        //newEvent.setPrivacy(privacy);
        newEvent.setDescription(description);
        // set event owner
        newEvent.setOwner(ParseUser.getCurrentUser());

        newEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Event Activity", "You just made a new event!");
                } else {
                    Log.d("Event Activity", "Something went wrong :(");
                    e.printStackTrace();
                }
            }
        });
    }
}
