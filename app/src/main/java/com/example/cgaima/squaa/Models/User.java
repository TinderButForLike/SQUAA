package com.example.cgaima.squaa.Models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@ParseClassName("User")
public class User extends ParseObject{

    private static final String KEY_IMAGE = "profile_picture";
    private static final String KEY_FRIENDS = "friends";
    private static final String KEY_USER = "username";


    //set user profile picture
    public void setProfilePic(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    //get user profile picture
    public ParseFile getProfilePicture() {
        return getParseFile(KEY_IMAGE);
    }

    // get user friends
    public ArrayList getFriends() {
        ArrayList<Object> attendees = new ArrayList<>();
        JSONArray jsonArray = (JSONArray)getJSONArray(KEY_FRIENDS);
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
    // set user friends
    public void setFriends(ParseUser attendee) {
        addUnique(KEY_FRIENDS,attendee);
        try {
            save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // get user name
    public String getUsername() {
        return getString(KEY_USER);
    }

    // set username
    public void setUsername(String name){
        put(KEY_USER, name);
    }


}