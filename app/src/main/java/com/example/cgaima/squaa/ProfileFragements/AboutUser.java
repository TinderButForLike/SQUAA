package com.example.cgaima.squaa.ProfileFragements;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class AboutUser extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private TextView Name;
    private TextView Age;
    private TextView Bio;
    private TextView home;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_about_user, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        Name = view.findViewById(R.id.tvName);
        Age = view.findViewById(R.id.tvAge);
        Bio = view.findViewById(R.id.tvBio);
        home = view.findViewById(R.id.tvResidence);


        Parcelable parcel = getActivity().getIntent().getParcelableExtra("event_owner");
        String name;
        Integer age;
        String bio;
        String hometown;

        if(parcel != null) {
            final Event event = (Event) Parcels.unwrap(parcel);
            ParseUser owner = event.getOwner();
            name = owner.getUsername().toString() + ',';


        } else {


            ParseUser currentUser = ParseUser.getCurrentUser();
            name = currentUser.getUsername().toString() + ',';
        }
            Name.setText(name);
            Age.setText("21");
            Bio.setText("I was born in Puerto Rico and moved to the United States when I was 4. I love to eat and bike around the " +
                    "city. Proudly latina");
            home.setText("San Andreas");

    }
}