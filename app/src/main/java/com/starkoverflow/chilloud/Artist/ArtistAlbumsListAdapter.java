package com.starkoverflow.chilloud.Artist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.starkoverflow.chilloud.Album.Album;
import com.starkoverflow.chilloud.Album.AlbumActivity;
import com.starkoverflow.chilloud.Album.AlbumsFragment;
import com.starkoverflow.chilloud.Album.AlbumsSongsListAdapter;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArtistAlbumsListAdapter extends RecyclerView.Adapter<ArtistAlbumsListAdapter.ViewHolder> {
    private ArrayList<Album> albums;
    private Context context;

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

        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            recyclerView = (RecyclerView) v.findViewById(R.id.albums_list);

            collapsedCard = (CardView) v.findViewById(R.id.collapsed_album_card);
            collapsedCardTitle = (TextView) v.findViewById(R.id.album_name);
            collapsedCardCover = (ImageView) v.findViewById(R.id.collapsed_album_cover);
            collapsedCardOverlay = (LinearLayout) v.findViewById(R.id.collapsed_album_card_overlay);
            collapsedCardBG = (LinearLayout) v.findViewById(R.id.collapsed_album_color);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtistAlbumsListAdapter(ArrayList<Album> albums, Context context) {
        this.albums = albums;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArtistAlbumsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.artist_albums_card, parent, false);
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

        holder.collapsedCardBG.setBackgroundColor(v.getContext().getColor(R.color.colorPrimary));

        Bitmap cover = BitmapFactory.decodeFile(albums.get(position).getArtPath());
        Palette palette;

        if (albums.get(position).getArtPath() != null)
            palette = createPaletteSync(cover);
        else
            palette = null;

        if (cover != null) {
            holder.collapsedCardCover.setImageBitmap(cover);
        } else {
            holder.collapsedCardCover.setImageResource(R.drawable.ic_album);
        }

        if (palette != null) {
            Palette.Swatch primary = palette.getDarkMutedSwatch();
            Palette.Swatch secondary = palette.getDarkVibrantSwatch();
            if (primary != null) {
                holder.collapsedCardBG.setBackgroundColor(primary.getRgb());
            } else if (secondary != null) {
                holder.collapsedCardBG.setBackgroundColor(secondary.getRgb());
            }
        }

        holder.collapsedCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra("album", albums.get(position));
                context.startActivity(intent);
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
        return albums.size();
    }
}
