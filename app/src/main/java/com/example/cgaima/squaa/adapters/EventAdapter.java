package com.example.cgaima.squaa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    Context context;
    List<Event> events;
    private final int REQUEST_CODE = 21;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.item_event, parent, false);
        return new ExpandableViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Event event = events.get(position);
        holder.event_name.setText(event.getEventName());
        holder.supporting_text.setText(event.getDescription());
        holder.location.setText(event.getLocation());
        //holder.tvDate.setText(event.getDate().toString());

        // TODO - set correct variables to UI
        //holder.tvAttendees.setText(event.getString("attendees"));
        //holder.tvDate.setText(event.getDate());
        //holder.tvLocation.setText(event.getLocation());

        if (event.getEventImage()==null) {
            Glide.with(context).load(
                    "https://www.google.com/url?sa=i&source=images&cd=&ved=" +
                            "2ahUKEwiKgcfT56TcAhWzHDQIHZ7ICZgQjRx6BAgBEAU&url=http%3A%2F%2F" +
                            "www.washingtonpost.com%2Frecipes%2Flunch-box-pasta-salad%2F15483%" +
                            "2F&psig=AOvVaw3QDPftuCv2CSZjHVzwoXZB&ust=1531871357428835")
                    .into(holder.media_image);
        } else {
            event.getEventImage().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.media_image.setImageBitmap(bmp);
                    }
                    else {
                        Log.d("EventAdapter", "Can't load image");
                        e.printStackTrace();
                    }
                }
            });
        }

        /*holder.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EventDetailActivity.class);
                i.putExtra("event", Parcels.wrap(event));
                //i.putExtra("post", post);
                ((Activity) context).startActivityForResult(i, REQUEST_CODE);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.media_image) ImageView media_image;
        @BindView(R.id.primary_text) TextView event_name;
        @BindView(R.id.supporting_text) TextView supporting_text;
        @BindView(R.id.sub_text) TextView date;
        @BindView(R.id.sub_text2) TextView location;
        /*@BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.tvLocation) TextView tvLocation;*/

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /*@Override
        public void onClick(View view) {
            final Event event = (Event) itemView.getTag();
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("event", Parcels.wrap(event));
            //TransitionInflater changeTransform = TransitionInflater.from(context);
            //TransitionInflater explodeTransform = TransitionInflater.from(context);
            //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, ivEventPic, "eventCard");
            context.startActivity(intent);//, options.toBundle());
        }*/
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public final class ExpandableViewHolder extends ViewHolder {
        @BindView(R.id.expand_button) ImageButton expandButton;
        @BindView(R.id.supporting_text) TextView supportingTextView;
        public ExpandableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.expand_button)
        public void onExpand() {
            if (supportingTextView.getVisibility() == View.VISIBLE) {
                expandButton.setImageResource(R.drawable.ic_expand_less_black_36dp);
                supportingTextView.setVisibility(View.GONE);


            }
            else {
                expandButton.setImageResource(R.drawable.ic_expand_more_black_36dp);
                supportingTextView.setVisibility(View.VISIBLE);

            }
        }
    }


}
