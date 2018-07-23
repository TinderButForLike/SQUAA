package com.example.cgaima.squaa;


import android.arch.paging.DataSource;

import com.example.cgaima.squaa.Models.Event;


public class ParseDataSourceFactory extends DataSource.Factory<Integer, Event> {

    @Override
    public DataSource<Integer, Event> create() {
        ParsePositionalDataSource source = new ParsePositionalDataSource();
        return source;
    }
}
