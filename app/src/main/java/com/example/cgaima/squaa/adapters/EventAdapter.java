package com.example.cgaima.squaa.adapters;

import android.app.Activity;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.activities.EventDetailActivity;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends PagedListAdapter<Event, EventAdapter.ViewHolder> {
    Context context;
    ArrayList<Event> events = new ArrayList<Event>();
    private final int REQUEST_CODE = 21;


    public static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK = new DiffUtil.ItemCallback<Event>() {

        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getObjectId() == newItem.getObjectId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            // TODO - add all contents
            return oldItem.getPrivacy()== newItem.getPrivacy()&&
            oldItem.getEventImage()== newItem.getEventImage()&&
            oldItem.getAttendees()== newItem.getAttendees();
        }
    };

    public EventAdapter() { super (DIFF_CALLBACK); }

    protected EventAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //final Event event = events.get(position);
        final Event event = getItem(position);

        if (event == null) { return; }

        holder.tvName.setText(event.getEventName());
//        holder.tvDate.setText(event.getDate().toString());
        holder.tvLocation.setText(event.getLocation());

        // TODO - set correct variables to UI
        //holder.tvAttendees.setText(event.getString("attendees"));
        //holder.tvDate.setText(event.getDate());
        //holder.tvLocation.setText(event.getLocation());

        if (event.getEventImage()==null) {
            Glide.with(context).load("https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwiKgcfT56TcAhWzHDQIHZ7ICZgQjRx6BAgBEAU&url=http%3A%2F%2Fwww.washingtonpost.com%2Frecipes%2Flunch-box-pasta-salad%2F15483%2F&psig=AOvVaw3QDPftuCv2CSZjHVzwoXZB&ust=1531871357428835").into(holder.ivPicture);
        } else {
            event.getEventImage().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.ivPicture.setImageBitmap(bmp);
                    }
                    else {
                        Log.d("EventAdapter", "Can't load image");
                        e.printStackTrace();
                    }
                }
            });
        }

        holder.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EventDetailActivity.class);
                i.putExtra("event", Parcels.wrap(event));
                //i.putExtra("post", post);
                ((Activity) context).startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    /*@Override
    public int getItemCount() {
        return events.size();
    }*/


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivPicture) ImageView ivPicture;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.tvLocation) TextView tvLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            final Event event = (Event) itemView.getTag();
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("event", Parcels.wrap(event));
            context.startActivity(intent);
        }
    }


    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public void add(Event event) {
        this.events.add(event);
        notifyItemInserted(events.size() - 1);
    }

    /*// for ListAdapter
    public void addMoreEvents(List<Event> newEvents) {
        this.events.addAll(newEvents);
        submitList(this.events); // DiffUtil takes care of the check
    }*/

}