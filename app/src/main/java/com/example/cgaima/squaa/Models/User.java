package com.example.cgaima.squaa.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {

    private static final String KEY_IMAGE = "profile_picture";
    private static final String KEY_FRIENDS = "friends";

    //set user profile picture
    public void setProfilePic(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    //get user profile picture
    public ParseFile getProfilePicture() {
        return getParseFile(KEY_IMAGE);
    }

    //set user friend list
    public void setFriendList(ParseFile friends) {
        put(KEY_FRIENDS, friends);
    }

    public ParseFile getFriendList() {
        return getParseFile(KEY_FRIENDS);
    }


}