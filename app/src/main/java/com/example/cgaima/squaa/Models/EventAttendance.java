package com.example.cgaima.squaa.Models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@ParseClassName("EventAttendance")
public class EventAttendance extends ParseObject {


    public void setEventAttendance(ParseUser user, Event event) {
        put("attendee", user);
        put("event", event);
    }

    public Event getEvent() {
        return (Event) getParseObject("event");
    }

    public static class Query extends ParseQuery {
        public Query() { super(EventAttendance.class); }
        // find specific event attendance with the user and event
        public Query findEventAttendance(ParseUser user, Event event) {
            whereEqualTo("attendee", user);
            whereEqualTo("event", event);
            return this;
        }

        // find specific event attendance with the user and event
        public Query findUserAttendance(ParseUser user) {
            whereEqualTo("attendee", user);
            return this;
        }


        // find all event attendance with all users
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

        public Query getUpcomingEvents(ParseUser user) {
            Calendar cal = Calendar.getInstance();
            Date today= cal.getTime();
            whereGreaterThanOrEqualTo("fromDate", today);
            return this;
        }


    }
}