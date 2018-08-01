package com.example.cgaima.squaa.Models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    // check if current user is attending event
    public static boolean isAttending(Event event) {
        EventAttendance.Query query = new EventAttendance.Query();
        query.findEventAttendance(ParseUser.getCurrentUser(), event);
        try {
            List eventAttendance = query.find();
            return !eventAttendance.isEmpty();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // defaults to not attending
        return false;
    }

    // get total number of attendees for event
    public static int getNumAttending(Event event) {
        // defaults num attending to 0
        int numAttending = 0;
        EventAttendance.Query query = new EventAttendance.Query();
        query.findAllEventAttendance(event);
        try {
            List eventAttendance = query.find();
            numAttending = eventAttendance.size();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numAttending;
    }
}