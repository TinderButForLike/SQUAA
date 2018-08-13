
package com.example.cgaima.squaa.Models;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Event")
public class Event extends ParseObject implements Place {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "event_image";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_ATTENDEES = "attendees";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "event_date";
    private static final String KEY_MESSAGES = "chat_messages";
    //
    private static final String KEY_PRIVACY = "public";
    private static final String KEY_NAME = "event_name";
    private static final String KEY_LATLNG = "latlng";
    LatLng mpk = new LatLng(37.4529, -122.148244);
    LatLng esb = new LatLng(40.7484, 73.9857);



    // get event name
    public String getEventName() {return getString(KEY_NAME);}
    // set event name
    public void setEventName(String name) {put(KEY_NAME,name);}
    // get the event description
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    // set the event description
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint(KEY_LATLNG);
    }
    public void setGeoPoint(ParseGeoPoint geoPoint) {
        put(KEY_LATLNG, geoPoint);
    }

    //    // get the event image
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
    public Date getDate() { return getDate(KEY_DATE); }

    public Date getToDate() { return getDate("toDate"); }
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
    public ArrayList getAttendees() {
        ArrayList<Object> attendees = new ArrayList<>();
        JSONArray jsonArray = (JSONArray)getJSONArray(KEY_ATTENDEES);
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0;i<len;i++){
                try {
                    attendees.add(jsonArray.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return attendees;
    }

    // set event attendees
    public void setAttendees(ParseUser attendee) {
          addUnique(KEY_ATTENDEES,attendee);
        try {
            save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // add cate

    //retrieve messages from event chat


    @Override
    public String getId() {
        return null;
    }

    @Override
    public List<Integer> getPlaceTypes() {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getAddress() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public CharSequence getName() {
        return null;
    }

    @Override
    public LatLng getLatLng() {
        return mpk;
    }

    @Nullable
    @Override
    public LatLngBounds getViewport() {
        return null;
    }

    @Nullable
    @Override
    public Uri getWebsiteUri() {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPhoneNumber() {
        return null;
    }

    @Override
    public float getRating() {
        return 0;
    }

    @Override
    public int getPriceLevel() {
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getAttributions() {
        return null;
    }

    @Override
    public Place freeze() {
        return null;
    }

    @Override
    public boolean isDataValid() {
        return false;
    }


    public static class Query extends ParseQuery {

        public Query() {
            super( Event.class);
        }

        public Query getTop() {
            setLimit(25);
            return this;
        }

        public Query withOwner() {
            include(KEY_OWNER);
            return this;
        }

        public Query containsWord(String query) {
            setLimit(25);
            whereContains(KEY_NAME, query);
            return this;
        }

        public Query getTopNear(ParseGeoPoint userLocation) {
            setLimit(10);
            whereNear("latlng", userLocation);
            return this;
        }
    }
}