package com.starkoverflow.chilloud.Search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.Artist.Artist;
import com.starkoverflow.chilloud.R;

import java.util.ArrayList;

public class SearchArtistsListAdapter extends RecyclerView.Adapter<SearchArtistsListAdapter.ViewHolder> {
    private ArrayList<Artist> artists;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout click;
        public TextView artist;
        public TextView info;
        public TextView library;
        public ImageView picture;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            click = v;
            artist = (TextView) v.findViewById(R.id.search_artist);
            info = (TextView) v.findViewById(R.id.search_artist_info);
            library = (TextView) v.findViewById(R.id.search_artist_library);
            picture = (ImageView) v.findViewById(R.id.search_artist_picture);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchArtistsListAdapter(ArrayList<Artist> artists) {
        this.artists=artists;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchArtistsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_artist_list_row, parent, false);
        // set the view's size, margins, paddings and layout parametersR.layout.drawer_list_row

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.artist.setText(artists.get(position).getArtist());
//        holder.info.setText(songs.get(position).getArtist() + " • " + songs.get(position).getDuration());
//        holder.album.setText(songs.get(position).getAlbum());
        if (artists.get(position).getCover() != null) {
            holder.picture.setImageBitmap(artists.get(position).getCover());
        } else {
            holder.picture.setImageResource(R.drawable.ic_people);
        }

//        holder.click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlaybackManager.playSong(v, songs.get(position));
//            }
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artists.size();
    }
}
