package com.example.cgaima.squaa.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String KEY_OWNER = "owner";
    public static final String EVENT_ID_KEY = "eventId";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public ParseUser getOwner() {
        return getParseUser(KEY_OWNER);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public void setOwner(ParseUser owner) {
        put(KEY_OWNER, owner);
    }

    public void setEvent(String eventId){
        put(EVENT_ID_KEY, eventId);
    }
    public String getEventId(){
        return getString(EVENT_ID_KEY);
    }

}