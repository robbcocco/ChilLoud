package com.starkoverflow.chilloud.Album;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;

import java.util.ArrayList;

public class AlbumsSongsListAdapter extends RecyclerView.Adapter<AlbumsSongsListAdapter.ViewHolder> {
    private ArrayList<Song> songs;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            mTitle = (TextView) v.findViewById(R.id.song_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumsSongsListAdapter(ArrayList<Song> songs) {
        this.songs=songs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumsSongsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_song_list_row, parent, false);
        // set the view's size, margins, paddings and layout parametersR.layout.drawer_list_row

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(songs.get(position).getTitle());

    }

    public Song getItem(int position) {
        return songs.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return songs.size();
    }
}
