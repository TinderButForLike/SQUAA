package com.example.cgaima.squaa.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.HomeFragment;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private List<Integer> mImages;
    private Context mContext;
    private List<String> mNames;

    public CategoriesAdapter(Context context, List<Integer> drawables, List<String> names) {
        mImages = drawables;
        mContext = context;
        mNames = names;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


      holder.name.setText(mNames.get(position));
      holder.catImage.setImageResource(mImages.get(position));

      holder.catImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Fragment homeFragment = HomeFragment.newInstance(holder.name.getText().toString());
              FragmentTransaction fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
              fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();

          }
      });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImage;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            catImage = (ImageView)itemView.findViewById(R.id.ivCategory);
            name = (TextView)itemView.findViewById(R.id.tvName);
        }
    }

}