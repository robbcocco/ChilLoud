package com.starkoverflow.chilloud.Album;

import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumsListAdapter extends RecyclerView.Adapter<AlbumsListAdapter.ViewHolder> {
    private ArrayList<Album> albums;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RecyclerView recyclerView;

        public CardView collapsedCard;
        public TextView collapsedCardTitle;
        public ImageView collapsedCardCover;
        public LinearLayout collapsedCardOverlay;
        public LinearLayout collapsedCardBG;

        public CardView expandedCard;
        public TextView expandedCardTitle;
        public ImageView expandedCardCover;
        public LinearLayout expandedCardHeader;
        public TextView expandedCardArtist;
        public LinearLayout expandedCardSongs;
        public LinearLayout expandedCardBG;

        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            recyclerView = (RecyclerView) v.findViewById(R.id.albums_list);

            collapsedCard = (CardView) v.findViewById(R.id.collapsed_album_card);
            collapsedCardTitle = (TextView) v.findViewById(R.id.album_name);
            collapsedCardCover = (ImageView) v.findViewById(R.id.collapsed_album_cover);
            collapsedCardOverlay = (LinearLayout) v.findViewById(R.id.collapsed_album_card_overlay);
            collapsedCardBG = (LinearLayout) v.findViewById(R.id.collapsed_album_color);

            expandedCard = (CardView) v.findViewById(R.id.expanded_album_card);
            expandedCardTitle = (TextView) v.findViewById(R.id.album_name_expanded);
            expandedCardCover = (ImageView) v.findViewById(R.id.expanded_album_cover);
            expandedCardHeader = (LinearLayout) v.findViewById(R.id.expanded_album_card_header);
            expandedCardArtist = (TextView) v.findViewById(R.id.album_artist);
            expandedCardSongs = (LinearLayout) v.findViewById(R.id.expanded_album_songs);
            expandedCardBG = (LinearLayout) v.findViewById(R.id.expanded_album_color);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumsListAdapter(ArrayList<Album> albums) {
        this.albums = albums;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.albums_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        View v = holder.itemView;
        holder.collapsedCardTitle.setText(albums.get(position).getAlbum());
        holder.expandedCardTitle.setText(albums.get(position).getAlbum());
        holder.expandedCardArtist.setText(albums.get(position).getArtist());

        if (albums.get(position).getCover() != null) {
            holder.collapsedCardCover.setImageBitmap(albums.get(position).getCover());
            holder.expandedCardCover.setImageBitmap(albums.get(position).getCover());
        } else {
            holder.collapsedCardCover.setImageResource(R.drawable.ic_album);
            holder.expandedCardCover.setImageResource(R.drawable.ic_album);
        }

        if (albums.get(position).getPalette() != null) {
            Palette.Swatch primary = albums.get(position).getPalette().getDarkMutedSwatch();
            Palette.Swatch secondary = albums.get(position).getPalette().getDarkVibrantSwatch();
            if (primary != null) {
                holder.collapsedCardBG.setBackgroundColor(primary.getRgb());
                holder.expandedCardBG.setBackgroundColor(primary.getRgb());
            } else if (secondary != null) {
                holder.collapsedCardBG.setBackgroundColor(secondary.getRgb());
                holder.expandedCardBG.setBackgroundColor(secondary.getRgb());
            } else {
                holder.collapsedCardBG.setBackgroundColor(v.getContext().getColor(R.color.colorPrimaryDark));
                holder.expandedCardBG.setBackgroundColor(v.getContext().getColor(R.color.colorPrimaryDark));
            }
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.album_songs_list);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Song> songs = albums.get(position).getSongs();
        Collections.sort(songs, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTrack() - b.getTrack();
            }
        });
        mAdapter = new AlbumsSongsListAdapter(songs);
        mRecyclerView.setAdapter(mAdapter);

        holder.collapsedCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.collapsedCard.setVisibility(View.GONE);
                holder.expandedCard.setVisibility(View.VISIBLE);
                AlbumsFragment.adjustSpanSize(position);
//                AlbumsFragment.adjustSpanSizeB();
            }
        });
        holder.expandedCardHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.collapsedCard.setVisibility(View.VISIBLE);
                holder.expandedCard.setVisibility(View.GONE);
                AlbumsFragment.adjustSpanSize(position);
//                AlbumsFragment.adjustSpanSizeB();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return albums.size();
    }
}
