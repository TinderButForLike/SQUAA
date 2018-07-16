package com.example.cgaima.squaa;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Event")
public class Event extends ParseObject {

    public static class Query extends ParseQuery<Event> {

        public Query() {
            super(Event.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }

}
