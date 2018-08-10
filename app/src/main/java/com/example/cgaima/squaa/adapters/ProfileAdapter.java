package com.example.cgaima.squaa.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgaima.squaa.DetailsTransition;
import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.Models.EventAttendance;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.EventDetailFragment;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    List<Event> mEvents;
    Context context;
    private final int REQUEST_CODE = 21;
    private final int REQUEST_CODE_1 = 22;
    // pass in the Tweets array in the constructor
    public ProfileAdapter(List<Event> events) {
        mEvents = events;

    }
    // for reach row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_event2, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
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

    // Add a list of items -- change to type used
    public void add(Event event) {
        mEvents.add(event);
        notifyItemInserted(mEvents.size()-1);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivEvent) ImageView ivEvent;
        public TextView tvDescription;


        //public ImageButton reply;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick
        public void launchEventDetails() {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Inflate transitions to apply
                    /*Transition changeTransform = TransitionInflater.from(context).
                            inflateTransition(R.transition.change_image_transform);
                    Transition explodeTransform = TransitionInflater.from(context).
                            inflateTransition(android.R.transition.explode);*/

                    // Setup exit transition on first fragment
                    eventDetailActivity.setSharedElementEnterTransition(new DetailsTransition());
                    eventDetailActivity.setEnterTransition(new Fade());
                    eventDetailActivity.setSharedElementReturnTransition(new DetailsTransition());
                    eventDetailActivity.setExitTransition(new Fade());
                }

                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addSharedElement(ivEvent, "eventCard")
                        .replace(R.id.fragment_container, eventDetailActivity)
                        .addToBackStack(null)
                        .commit();
            }

        }
    }


}

