package com.example.cgaima.squaa.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.activities.MapsActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class CreateEventFragment extends Fragment {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.location)
    EditText location;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.eventPic)
    ImageView eventPic;
    @BindView(R.id.privateCheck)
    CheckBox privateCheck;
    @BindView(R.id.publicCheck)
    CheckBox publicCheck;


    static ParseFile image;
    private int PICK_PHOTO_CODE = 1046;

    // Required empty public constructor
    public CreateEventFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView tv = toolbar.findViewById(R.id.toolbar_title);
        tv.setText("create an event");
        // Inflate the layout for this fragment
        Log.e("EventFragment", "I get created too");
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, view);
        //get the spinner from the xml.
        Spinner dropdown = view.findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        location.setText(getActivity().getIntent().getStringExtra("locationtext"));
        if (getActivity().getIntent().getParcelableExtra("geo") != null) {
            Log.d("CreateEventFrag", String.valueOf(((ParseGeoPoint) (getActivity().getIntent().getExtras().getParcelable("geo"))).getLatitude()));
        }
    }

    //launch the map
    @OnClick(R.id.mapLaunch)
    public void launchMap() {
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        startActivityForResult(mapIntent, 30);

    }

    // TODO - automatically add new event to home without refresh
    @OnClick(R.id.create)
    public void onCreateEvent() {
        String mName = name.getText().toString();
        String mLocation = location.getText().toString();
        String mDescription = description.getText().toString();

        Boolean mPrivacy;
        if (privateCheck.isChecked()) {mPrivacy = true; }
        else { mPrivacy = false; }

        ParseFile mImage = image;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        String mDate = month + "/" + day + "/" + year;


        ParseGeoPoint mParseGeoPoint = getActivity().getIntent().getParcelableExtra("geo");
//        mParseGeoPoint.setLatitude(eventLatLng.latitude);
//        mParseGeoPoint.setLongitude(eventLatLng.longitude);

        if (mParseGeoPoint != null) {
            createEvent(mName, mLocation, mDescription, mPrivacy, mImage, mDate, mParseGeoPoint);
        } else {
            Log.d("CreateEventFrag", "still null");
        }

        // TODO - set fragment to home fragment after creating event
        /*Fragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.viewpager, homeFragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();*/
    }

    //create a new event
    private void createEvent(String name, String location, String description, boolean privacy, ParseFile img, String date, ParseGeoPoint parseGeoPoint) { //TODO add privacy, image, date
        final Event newEvent = new Event();
        newEvent.setEventName(name);
        newEvent.setLocation(location);
        newEvent.setDate(date);
        newEvent.setPrivacy(privacy);
        newEvent.setDescription(description);
        newEvent.setEventImage(img);
        newEvent.setGeoPoint(parseGeoPoint);
        newEvent.put("numRating", 0);

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

    @OnClick(R.id.eventPic)
    //choose a photo from the gallery
    public void onPickPhoto() {
        // create the intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //as long as the result isn't null, we can use this intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            //bring up the gallery
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    //handle intents and activity results
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {
                //launch the gallery
                if (data != null) {
                    Uri photoUri = data.getData();
                    //do something with the photo based on what we get
                    Bitmap selectedImage = null;
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    } catch (IOException e) {
                        Log.d("Event Activity", "Something aint right");
                        e.printStackTrace();
                    }

                    //load the image into the view
                    eventPic.setImageBitmap(selectedImage);
                    writeBitmapToFile(selectedImage);

                }
            }
        }
        else {
            //make the user sad
            Toast.makeText(getContext(), "there was an error uploading your picture. try again!", Toast.LENGTH_SHORT).show();
        }
    }
    public static void writeBitmapToFile(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] bitmapBytes = stream.toByteArray();

        image = new ParseFile("myImage", bitmapBytes);
        try {
            image.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
