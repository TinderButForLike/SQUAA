
package com.example.cgaima.squaa.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Date;

@ParseClassName("Post")
public class Event extends ParseObject{
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "event_image";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_ATTENDEES = "attendees";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "date";
    private static final String KEY_PRIVACY = "privacy";



    // get the event description
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    // set the event description
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    // get the event image
    public ParseFile getEventImage() {
        return getParseFile(KEY_IMAGE);
    }
    //set the event image
    public void setEventImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }
    // get the event owner
    public ParseUser getOwner() {
        return getParseUser(KEY_OWNER);
    }
    //set the event owner
    public void setOwner(ParseUser user) {
        put(KEY_OWNER, user);
    }

    // Set event date
    public void setDate(Date date){
        put(KEY_DATE, date);
    }

    // get event date
    public Date getDate() {
        return getDate(KEY_DATE);
    }
    // get event location
    public String getLocation() {
        return getString(KEY_LOCATION);
    }
    // Set event location
    public void setLocation(String location){
        put(KEY_LOCATION, location);
    }
    // get event privacy
    public Boolean getPrivacy() {
        return getBoolean(KEY_PRIVACY);
    }

    // set event privacy
    public void setPrivacy(Boolean privacy) {
        put(KEY_PRIVACY, privacy);
    }

    // get event attendees
//    public ArrayList<User> getAttendees() {
//        return (KEY_ATTENDEES);
//    }
    // set event attendees
    public void setAttendees(JSONArray attendees) {
        put(KEY_ATTENDEES, attendees);
    }


    public static class Query extends ParseQuery{

        public Query() {
            super( Event.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withOwner() {
            include("owner");
            return this;
        }

    }

}