package com.example.cgaima.squaa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.EventDetailFragment;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {
    private List<Event> mEvents;
    Context context;

    // pass in the Tweets array in the constructor
    public UpcomingAdapter(List<Event> events) {
        mEvents = events;

    }
    // for reach row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_upcoming, parent, false);
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
        holder.tvName.setText(event.getEventName());
        //holder.tvTime.setText(event.getToDate().toString());
        Random rand = new Random();
        int n = rand.nextInt(10 ) + 1;
       holder.tvCountdown.setText(String.valueOf(n));

        Glide.with(context).load(event.getEventImage().getUrl()).into(holder.ivEvent);


    }

    @Override
    public int getItemCount() {
        return mEvents.size();

    }

    public void add(Event event) {
        // Add a list of items -- change to type used

        mEvents.add(event);
        notifyItemInserted(mEvents.size()-1);

    }

    // create the ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivEvent;
        public TextView tvName;
        public TextView tvCountdown;
        public TextView tvTime;


        //public ImageButton reply;

        public ViewHolder(View itemView) {
            super(itemView);

            ivEvent = (ImageView) itemView.findViewById(R.id.ivImage);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCountdown = (TextView) itemView.findViewById(R.id.tvCountdown);
            tvTime = (TextView) itemView.findViewById(R.id.tvDate);
            ButterKnife.bind(this, itemView);

        }
        @OnClick
        public void launchEventDetails() {
            Log.e("UpcomingAdapter", "hi i'm being clicked");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Event event = mEvents.get(position);
                EventAttendance eventAttendance = null;
                try {
                    eventAttendance = (EventAttendance) new EventAttendance.Query().findEventAttendance(ParseUser.getCurrentUser(), event).getFirst();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Fragment eventDetailActivity = EventDetailFragment.newInstance(event, eventAttendance);

                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, eventDetailActivity)
                        .addToBackStack(null)
                        .commit();
            }
        }


    }


}

