package com.example.cgaima.squaa.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class CreateEventFragment extends Fragment {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.location)
    EditText location;
    //@BindView(R.id.date)
    //EditText date;
    @BindView(R.id.privacy)
    EditText privacy;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.eventPic)
    ImageView eventPic;

    static ParseFile image;
    private int PICK_PHOTO_CODE = 1046;

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

    // TODO - automatically add new event to home without refresh
    @OnClick(R.id.create)
    public void onCreateEvent() {
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
    private void createEvent(String name, String location, String description) { // TODO add privacy, image, date
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

    @OnClick(R.id.launchGalBtn)
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
            //make them sad
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
