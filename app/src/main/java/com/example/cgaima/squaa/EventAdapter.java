package com.example.cgaima.squaa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgaima.squaa.Models.Event;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    Context context;
    List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // TODO - create get and set methods in Event class?
        Event event = events.get(position);
        //holder.tvName.setText(event.get);
        holder.tvDescription.setText(event.getDescription());

        // TODO - is attendees a JSONArray?
        //holder.tvAttendees.setText(event.getString("attendees"));
        //holder.tvDate.setText(event.getDate());
        //holder.tvLocation.setText(event.getLocation());
        // TODO - what key for picture
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

    @Override
    public int getItemCount() {
        return events.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPicture) ImageView ivPicture;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvAttendees) TextView tvAttendees;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.tvLocation) TextView tvLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }
}
