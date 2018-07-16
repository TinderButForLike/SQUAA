package com.example.cgaima.squaa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.cgaima.squaa.Models.Event;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    //resource variables
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this); //bind butterknife after

        if (useToolbar()){
            setActionBar(toolbar);
        }
        else {
            toolbar.setVisibility(View.GONE);
        }
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //create a new event
    private void createEvent(String name, String location, Date date, boolean privacy, String description) {
        final Event newEvent = new Event();
        newEvent.setEventName(name);
        newEvent.setLocation(location);
        newEvent.setDate(date);
        newEvent.setPrivacy(privacy);
        newEvent.setDescription(description);

        newEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Event Activity", "You just made a new event!");
                }
                else {
                    Log.d("Event Activity", "Something went wrong :(");
                    e.printStackTrace();
                }
            }
        });
    }






    protected boolean useToolbar(){
        return true;
    }



}
