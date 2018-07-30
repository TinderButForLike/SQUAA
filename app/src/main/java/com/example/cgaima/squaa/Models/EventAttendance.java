package com.example.cgaima.squaa.Models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("EventAttendance")
public class EventAttendance extends ParseObject {
    public void setEventAttendance(ParseUser user, Event event) {
        addUnique("attendee", user);
        addUnique("event", event);
    }

    public static class Query extends ParseQuery {
        public Query() { super(EventAttendance.class); }
        // find specific event attendance with the user and event
        public Query findEventAttendance(ParseUser user, Event event) {
            whereEqualTo("attendee", user);
            whereEqualTo("event", event);
            return this;
        }
        // find all event attendance
        public Query findAllEventAttendance(Event event) {
            whereEqualTo("event", event);
            return this;
        }
        // TODO - find profile event history

        // TODO - find profile upcoming events

        public Query includeDetails() {
            include("attendee");
            include("event");
            return this;
        }
    }


}