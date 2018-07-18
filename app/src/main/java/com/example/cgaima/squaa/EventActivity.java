package com.example.cgaima.squaa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cgaima.squaa.Models.Event;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    //resource variables

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
    @BindView(R.id.create)
    Button create;
    @BindView(R.id.launchGalBtn)
    Button launch;
    @BindView(R.id.eventPic)
    ImageView eventPic;

    static ParseFile image;

    private int PICK_PHOTO_CODE = 1046;
    final Event newEvent = new Event();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this); //bind butterknife after

        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mName = name.getText().toString();
                String mLocation = location.getText().toString();
                String mDescription = description.getText().toString();
                ParseFile mImage = image;

                createEvent(mName, mLocation, mDescription, mImage);

                Intent createIntent = new Intent(EventActivity.this, HomeActivity.class);
                startActivity(createIntent);
            }
        });
    }

    //create a new event
    private void createEvent(String name, String location, String description, ParseFile img) { //TODO add privacy, image,chey date
        newEvent.setEventName(name);
        newEvent.setLocation(location);
        newEvent.setDescription(description);
        newEvent.setEventImage(img);
        // set event owner
        newEvent.setOwner(ParseUser.getCurrentUser());

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

    //choose a photo from the gallery
    public void onPickPhoto() {
        // create the intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //as long as the result isn't null, we can use this intent
        if (intent.resolveActivity(getPackageManager()) != null) {
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
                         selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
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
            Toast.makeText(this, "there was an error uploading your picture. try again!", Toast.LENGTH_SHORT).show();
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


    protected boolean useToolbar(){
        return true;
    }



}
