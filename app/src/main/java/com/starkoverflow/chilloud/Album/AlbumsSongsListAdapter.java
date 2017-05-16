package com.starkoverflow.chilloud.Album;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.Artist.ArtistActivity;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;
import com.starkoverflow.chilloud.classes.PlaybackManager;

import java.util.ArrayList;

public class AlbumsSongsListAdapter extends RecyclerView.Adapter<AlbumsSongsListAdapter.ViewHolder> {
    private ArrayList<Song> songs;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout click;
        public TextView title;
        public TextView track;
        public TextView duration;
        public ImageButton options;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            click = v;
            title = (TextView) v.findViewById(R.id.album_song_title);
            track = (TextView) v.findViewById(R.id.album_song_number);
            duration = (TextView) v.findViewById(R.id.album_song_duration);
            options = (ImageButton) v.findViewById(R.id.album_song_options);
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(songs.get(position).getTitle());
        holder.track.setText(Integer.toString(position+1));
//        holder.track.setText(Integer.toString(songs.get(position).getTrack()));
        holder.duration.setText(songs.get(position).getDuration());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackManager.playSong(v, songs.get(position));
            }
        });
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Options", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
