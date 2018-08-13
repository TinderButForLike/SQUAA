package com.example.cgaima.squaa.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.ProfileFragements.Upcoming;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.activities.HomeActivity;
import com.example.cgaima.squaa.activities.MapsActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class CreateEventFragment extends Fragment {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.eventPic)
    ImageView eventPic;
    @BindView(R.id.privateCheck)
    CheckBox privateCheck;
    @BindView(R.id.publicCheck)
    CheckBox publicCheck;
    @BindView(R.id.pickDate)
    ImageButton pickDate;
    @BindView(R.id.mapLaunch)
    ImageButton mapLaunch;


    int mYear;
    int mMonth;
    int mDay;
    boolean mPrivacy;
    String locText;
    ParseGeoPoint mParseGeoPoint;

    Calendar cal = Calendar.getInstance();
    static ParseFile image;
    private static int PICK_PHOTO_CODE = 1046;


    Date theDate = new Date();

    // Required empty public constructor
    public CreateEventFragment() { }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //configure the channel!! a channel is needed to create a space for notifs to pass between the server and the device
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("myChannelId", "My Channel", importance);
        channel.setDescription("Reminders");

        //register the channel with the notifications manager
        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);


        //broadcast managers that handle intent passes with no dependency on the lifecycle of the activity
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverDate, new IntentFilter("yearIntent"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverMap, new IntentFilter("infoIntent"));

        privateCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mPrivacy = true;
                    publicCheck.setEnabled(false);
                }
                if (!b) {
                    publicCheck.setEnabled(true);
                }
            }
        });
        publicCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mPrivacy = false;
                    privateCheck.setEnabled(false);
                }
                if (!b) {
                    privateCheck.setEnabled(true);
                }
            }
        });
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

    //launch the map
    @OnClick(R.id.mapLaunch)
    public void launchMap() {
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        startActivityForResult(mapIntent, 30);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //Handles DatePickerFragment broadcast intents
    //Allows the intents to be passed without starting a new activity!!
    BroadcastReceiver broadcastReceiverDate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mYear = intent.getIntExtra("year", cal.get(Calendar.YEAR));
            mDay = intent.getIntExtra("day", cal.get(Calendar.DAY_OF_MONTH));
            mMonth = intent.getIntExtra("month" , cal.get(Calendar.MONTH)) +1;
            theDate.setTime(intent.getLongExtra("date", -1));
            dateRef();
        }
    };

    BroadcastReceiver broadcastReceiverMap = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            locText = intent.getStringExtra("locationtext");
            mParseGeoPoint = intent.getParcelableExtra("geo");
            locRef();
        }
    };

    public void locRef() {
        location.setText(locText);
    }
    public void dateRef() {

        date.setText(mMonth + "/" + mDay + "/" + mYear);
        Log.d("the date is:", theDate.toString());
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
        String mDate = mMonth + "/" + mDay + "/" + mYear;

        if (mParseGeoPoint != null) {
            createEvent(mName, mLocation, mDescription, mPrivacy, mImage, theDate, mParseGeoPoint);
        } else {
            Log.d("CreateEventFrag", "still null");
        }

        createNotification(4, R.drawable.calendar, "You just made an event!", "Click to view");

        Intent back = new Intent(getContext(), HomeActivity.class);
        startActivity(back);

        // TODO - set fragment to home fragment after creating event
//        Fragment homeFragment = new HomeFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.vpContainer, homeFragment ); // give your fragment container id in first parameter
//        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//        transaction.commit();
    }

    //create a new event
    private void createEvent(String name, String location, String description, boolean privacy, ParseFile img, Date date, ParseGeoPoint parseGeoPoint) { //TODO add privacy, image, date
        final Event newEvent = new Event();
        newEvent.setEventName(name);
        newEvent.setLocation(location);
        newEvent.setDate(date);
        newEvent.setFromDate(date);
        newEvent.setToDate(date);
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

    //launch the calendar date picker dialog
    @OnClick(R.id.pickDate)
    public void onPickDate() {
        showDatePickerDialog();
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

    //call the show() method on a new instance of the DialogFragment
    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
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

    //this method converts  bitmap to a parsefile so that we can save internal storage content in parse
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

    private void createNotification(int nId, int iconRes, String title, String body) {

        //intent trigger when something is selected
        Intent intent = new Intent(getContext(), Upcoming.class);
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notifs with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; //cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), requestID, intent, flags); //gets an activity through its intent

        //attach the pending intent to a new notif
        Notification mBuilder =
                new NotificationCompat.Builder(getContext(), "myChannelId")
                        .setSmallIcon(iconRes)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true) //hides notif after selection
                        .setWhen(System.currentTimeMillis())
                        .build();

        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, mBuilder); //update the notif later

    }
}
