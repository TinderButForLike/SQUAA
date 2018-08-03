package com.example.cgaima.squaa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cgaima.squaa.R;

import butterknife.ButterKnife;

public class PastEventFragment extends Fragment {

    // Required empty public constructor
    public PastEventFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_event, container, false);
        ButterKnife.bind(this, view);
        // inflate the layout for this fragment
        return view;
    }
}