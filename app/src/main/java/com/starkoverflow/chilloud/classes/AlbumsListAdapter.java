package com.starkoverflow.chilloud.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
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

import com.starkoverflow.chilloud.MainActivity;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.fragments.AlbumsFragment;

import java.util.ArrayList;

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
        public CardView card;
        public TextView mTitle;
        public TextView mTitleB;
        public ImageView collapsedCover;
        public LinearLayout collapsedCard;
        public LinearLayout collapsedCardOverlay;
        public ImageView expandedCover;
        public LinearLayout expandedCard;
        public LinearLayout expandedCardHeader;
        public LinearLayout expandedcardSongs;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            card = (CardView) v.findViewById(R.id.card_view);
            mTitle = (TextView) v.findViewById(R.id.album_name);
            mTitleB = (TextView) v.findViewById(R.id.album_name_expanded);
            collapsedCover = (ImageView) v.findViewById(R.id.collapsed_album_cover);
            collapsedCard = (LinearLayout) v.findViewById(R.id.collapsed_album_card);
            collapsedCardOverlay = (LinearLayout) v.findViewById(R.id.collapsed_album_card_overlay);
            expandedCover = (ImageView) v.findViewById(R.id.expanded_album_cover);
            expandedCard = (LinearLayout) v.findViewById(R.id.expanded_album_card);
            expandedCardHeader = (LinearLayout) v.findViewById(R.id.expanded_album_card_header);
            expandedcardSongs = (LinearLayout) v.findViewById(R.id.expanded_album_songs);
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
        holder.mTitle.setText(albums.get(position).getAlbum());
        holder.mTitleB.setText(albums.get(position).getAlbum());

        if (albums.get(position).getArtPath() != null) {
            holder.collapsedCover.setImageBitmap(albums.get(position).getCover());
            holder.expandedCover.setImageBitmap(albums.get(position).getCover());
//            holder.collapsedCover.setImageURI(Uri.parse(albums.get(position).getArtPath()));
        } else {
            holder.collapsedCover.setImageResource(R.drawable.ic_album);
            holder.expandedCover.setImageResource(R.drawable.ic_album);
        }

        if (albums.get(position).getPalette() != null) {
            Palette.Swatch primary = albums.get(position).getPalette().getMutedSwatch();
            Palette.Swatch secondary = albums.get(position).getPalette().getDarkMutedSwatch();
            if (primary != null) {
                holder.collapsedCard.setBackgroundColor(primary.getRgb());
                holder.expandedCard.setBackgroundColor(primary.getRgb());
                holder.card.setCardBackgroundColor(primary.getRgb());
            } else if (secondary != null) {
                holder.collapsedCard.setBackgroundColor(secondary.getRgb());
                holder.expandedCard.setBackgroundColor(secondary.getRgb());
                holder.card.setCardBackgroundColor(secondary.getRgb());
            }
        }

        holder.collapsedCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.collapsedCard.setVisibility(View.GONE);
                holder.expandedCard.setVisibility(View.VISIBLE);
                AlbumsFragment.adjustSpanSize(position);
            }
        });
        holder.expandedCardHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.collapsedCard.setVisibility(View.VISIBLE);
                holder.expandedCard.setVisibility(View.GONE);
                AlbumsFragment.adjustSpanSize(position);
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.album_songs_list);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SongsListAdapter(albums.get(position).getSongs());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        v.getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Snackbar.make(view, "Do something", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return albums.size();
    }
}
