package com.example.cgaima.squaa.adapters;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import com.example.cgaima.squaa.Models.Event;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ParsePositionalDataSource extends PositionalDataSource<Event> {
    public ParseQuery<Event> getQuery() {
        return ParseQuery.getQuery(Event.class).orderByDescending("createdAt");
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Event> callback) {
        ParseQuery<Event> query = getQuery();

        // Use values passed when PagedList was created.
        query.setLimit(params.requestedLoadSize);
        query.setSkip(params.requestedStartPosition);

        try {
            // loadInitial() should run queries synchronously so the initial list will not be empty
            // subsequent fetches can be async
            int count = query.count();
            List<Event> events = query.find();

            // return info back to PagedList
            callback.onResult(events, params.requestedStartPosition, count);
        } catch (ParseException e) {
            e.printStackTrace();
            // retry logic here
        }
    }


    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Event> callback) {
        // get basic query
        ParseQuery<Event> query = getQuery();

        query.setLimit(params.loadSize);

        // fetch the next set from a different offset
        query.setSkip(params.startPosition);

        try {
            // run queries synchronously since function is called on a background thread
            List<Event> events = query.find();

            // return info back to PagedList
            callback.onResult(events);
        } catch (ParseException e) {
            // retry logic here
        }

    }
}
