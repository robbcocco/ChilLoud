package com.starkoverflow.chilloud.Artist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;
import com.starkoverflow.chilloud.classes.PlaybackManager;

import java.util.ArrayList;

public class ArtistSongsListAdapter extends RecyclerView.Adapter<ArtistSongsListAdapter.ViewHolder> {
    private ArrayList<Song> songs;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout click;
        public TextView title;
        public TextView info;
        public ImageView cover;
        public ImageButton options;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            click = v;
            title = (TextView) v.findViewById(R.id.song_title);
            info = (TextView) v.findViewById(R.id.song_info);
            cover = (ImageView) v.findViewById(R.id.song_cover);
            options = (ImageButton) v.findViewById(R.id.song_options);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtistSongsListAdapter(ArrayList<Song> songs) {
        this.songs=songs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArtistSongsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.song_list_row, parent, false);
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
        holder.info.setText(songs.get(position).getDuration());

        Bitmap cover = BitmapFactory.decodeFile(songs.get(position).getArtPath());

        if (cover != null) {
            holder.cover.setImageBitmap(cover);
        } else {
            holder.cover.setImageResource(R.drawable.ic_album);
        }

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

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return songs.size();
    }
}
