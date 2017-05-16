package com.starkoverflow.chilloud.Artist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;

import java.net.URL;
import java.util.ArrayList;

public class ArtistsListAdapter extends RecyclerView.Adapter<ArtistsListAdapter.ViewHolder> {
    private static final String TAG = "Artists Adapter";
    private ArrayList<Artist> artists;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView artistCard;
        public TextView artist;
        public ImageView artistPicture;
        public LinearLayout artistOverlay;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            artistCard = (CardView) v.findViewById(R.id.artist_card);
            artist = (TextView) v.findViewById(R.id.artist_name);
            artistPicture = (ImageView) v.findViewById(R.id.artist_picture);
            artistOverlay = (LinearLayout) v.findViewById(R.id.artist_card_overlay);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtistsListAdapter(ArrayList<Artist> artists, Context context) {
        this.artists = artists;
        this.context=context;
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
        holder.artist.setText(artists.get(position).getArtist());
        holder.artistPicture.setImageResource(R.drawable.ic_people);

        FetchArtistInfo fetcher = new FetchArtistInfo();
        fetcher.artistCard=holder.artistCard;
        fetcher.artistPicture=holder.artistPicture;
        fetcher.execute(artists.get(position));

        holder.artistOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArtistActivity.class);
                intent.putExtra("artist", artists.get(position));
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artists.size();
    }

    private class FetchArtistInfo extends AsyncTask<Artist, Integer, Artist> {
        CardView artistCard;
        ImageView artistPicture;
        // Do the long-running work in here
        protected Artist doInBackground(Artist... artists) {
            int count = artists.length;
            for (int i = 0; i < count; i++) {
                artists[i].getArtistUrl();
                artists[i].getBitmapFromURL();
                // Escape early if cancel() is called
                if (isCancelled()) break;

                return artists[i];
            }
            return null;
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {

        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(Artist artist) {
            if (artist.getCover() != null) {
                artistPicture.setImageBitmap(artist.getCover());

                Palette.Swatch primary = artist.getPalette().getDarkMutedSwatch();
                Palette.Swatch secondary = artist.getPalette().getDarkVibrantSwatch();
                if (primary != null) {
                    artistCard.setCardBackgroundColor(primary.getRgb());
                } else if (secondary != null) {
                    artistCard.setCardBackgroundColor(secondary.getRgb());
                }
            }
        }
    }
}
