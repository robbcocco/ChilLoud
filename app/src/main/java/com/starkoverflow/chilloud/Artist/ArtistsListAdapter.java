package com.starkoverflow.chilloud.Artist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;

import java.util.ArrayList;

public class ArtistsListAdapter extends RecyclerView.Adapter<ArtistsListAdapter.ViewHolder> {
    private static final String TAG = "Artists Adapter";
    private ArrayList<Artist> artists;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public ImageView artistPicture;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            mTitle = (TextView) v.findViewById(R.id.artist_name);
            artistPicture = (ImageView) v.findViewById(R.id.artist_picture);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtistsListAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArtistsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.artists_card, parent, false);
        // set the view's size, margins, paddings and layout parametersR.layout.drawer_list_row

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(artists.get(position).getArtist());
        if (artists.get(position).getCover() != null) {
            holder.artistPicture.setImageBitmap(artists.get(position).getCover());
        } else {
            holder.artistPicture.setImageResource(R.drawable.ic_people);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artists.size();
    }
}
