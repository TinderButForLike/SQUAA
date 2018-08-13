package com.example.cgaima.squaa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.adapters.CategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Categories extends Fragment {
    @BindView(R.id.rvCats) RecyclerView rvGrid;
    private CategoriesAdapter mAdapter;
    private List<String> names;
    private List<Integer> mImages;


    private FragmentActivity listener;

    // Required empty public constructor
    public Categories() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("EventHistory", "Event history fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);

        rvGrid = view.findViewById(R.id.rvCats);

        // allows for optimizations
        rvGrid.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getActivity(), 2);

        rvGrid.setLayoutManager(layout);

        mImages = new ArrayList<>();
        names = new ArrayList<>();

        mImages.add(R.drawable.cat_sportfitness);
        mImages.add(R.drawable.cat_outdoors);
        mImages.add(R.drawable.cat_games);
        mImages.add(R.drawable.cat_foodanddrink);
        mImages.add(R.drawable.cat_musictheatre);
        mImages.add(R.drawable.cat_museumandart);
        mImages.add(R.drawable.cat_movies);
        mImages.add(R.drawable.cat_nightlife);
        mImages.add(R.drawable.cat_natureandparks);
        mImages.add(R.drawable.cat_shopping);
        mImages.add(R.drawable.cat_work);

        names.add("Sport and Fitness");
        names.add( "Outdoors");
        names.add("Games");
        names.add("Food and Drink");
        names.add("Music and Theatre");
        names.add("Museums and Art");
        names.add("Movies");
        names.add("Nightlife");
        names.add("Nature and Parks");
        names.add("Shopping");
        names.add("Work");



        // Create an adapter
        mAdapter = new CategoriesAdapter(getContext(),mImages,names);

        // Bind adapter to list
        rvGrid.setAdapter(mAdapter);

        return view;
    }


}

