package com.starkoverflow.chilloud.Artist;

import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.starkoverflow.chilloud.Album.Album;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;

import java.util.ArrayList;

public class ArtistActivity extends AppCompatActivity {

    private RecyclerView albumRecyclerView;
    private RecyclerView.Adapter albumAdapter;
    private RecyclerView.LayoutManager albumLayoutManager;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        Artist artist = getIntent().getParcelableExtra("artist");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitle(artist.getArtist());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView cover = (ImageView) findViewById(R.id.cover);
        ImageView coverbg = (ImageView) findViewById(R.id.cover_bg);
        LinearLayout infoBg = (LinearLayout) findViewById(R.id.info);

        FetchArtistInfo fetcher = new FetchArtistInfo();
        fetcher.info=infoBg;
        fetcher.artistPicture=cover;
        fetcher.artistPictureBG=coverbg;
        fetcher.execute(artist);


        albumRecyclerView = (RecyclerView) findViewById(R.id.arist_albums_list);
        // use a linear layout manager
        albumLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        albumRecyclerView.setLayoutManager(albumLayoutManager);
        albumRecyclerView.setHasFixedSize(false);
        albumAdapter = new ArtistAlbumsListAdapter(artist.getAlbum(), this);
        albumRecyclerView.setAdapter(albumAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.artist_songs_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Song> songs = new ArrayList<>();
        for (int i = 0; i < artist.getAlbum().size(); i++) {
            Album album = artist.getAlbum().get(i);
            for (int j = 0; j < album.getSongs().size(); j++) {
                songs.add(album.getSongs().get(j));
            }
        }
        mAdapter = new ArtistSongsListAdapter(songs);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class FetchArtistInfo extends AsyncTask<Artist, Integer, Artist> {
        LinearLayout info;
        ImageView artistPicture;
        ImageView artistPictureBG;
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
                artistPictureBG.setImageBitmap(artist.getCover());

                Palette.Swatch primary = artist.getPalette().getDarkMutedSwatch();
                Palette.Swatch secondary = artist.getPalette().getDarkVibrantSwatch();
                if (primary != null) {
                    info.setBackgroundColor(primary.getRgb());
//                    titleBg.setBackgroundColor(primary.getRgb());
                    collapsingToolbarLayout.setContentScrimColor(primary.getRgb());
                    collapsingToolbarLayout.setBackgroundColor(primary.getRgb());
                } else if (secondary != null) {
                    info.setBackgroundColor(secondary.getRgb());
//                    titleBg.setBackgroundColor(secondary.getRgb());
                    collapsingToolbarLayout.setContentScrimColor(secondary.getRgb());
                    collapsingToolbarLayout.setBackgroundColor(secondary.getRgb());
                }
            }
        }
    }
}
