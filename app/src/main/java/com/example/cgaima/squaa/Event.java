package com.example.cgaima.squaa;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Event")
public class Event extends ParseObject {

    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PRIVACY = "privacy";
    private static final String KEY_ATTENDEES = "attendees";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }
    //TODO: setLocation()
    public String getDate() {
        return getString(KEY_DATE);
    }
    public void setDate(String date) {
        put(KEY_DATE, date);
    }
    public String getType() {
        return getString(KEY_TYPE);
    }
    public void setType(String type) {
        put(KEY_TYPE, type);
    }
    public boolean getPrivacy(){
        return getBoolean(KEY_PRIVACY);
    }
    public void setPrivacy(boolean privacy){
        put(KEY_PRIVACY, privacy);
    }
    //TODO: getAttendees, setAttendees

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }
}
