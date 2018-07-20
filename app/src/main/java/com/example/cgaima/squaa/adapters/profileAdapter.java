package com.example.cgaima.squaa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;

import java.util.List;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ViewHolder> {
    private List<Event> mEvents;
    Context context;
    private final int REQUEST_CODE = 21;
    private final int REQUEST_CODE_1 = 22;
    // pass in the Tweets array in the constructor
    public profileAdapter(List<Event> events) {
        mEvents = events;

    }
    // for reach row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_event2, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mEvents.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Event> list) {
        mEvents.addAll(list);
        notifyDataSetChanged();
    }


    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        final Event event = mEvents.get(position);

        // populate the views according to this data


        Glide.with(context).load(event.getEventImage().getUrl()).into(holder.ivEvent);


    }

    @Override
    public int getItemCount() {
        return mEvents.size();

    }

    // create the ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivEvent;
        public TextView tvUsername;
        public TextView tvDescription;
        //public ImageButton reply;

        public ViewHolder(View itemView) {
            super(itemView);

            ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);

        }



    }


}

