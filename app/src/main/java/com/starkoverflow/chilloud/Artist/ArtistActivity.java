package com.starkoverflow.chilloud.Artist;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.Album.Album;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;
import com.starkoverflow.chilloud.classes.PlaybackManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArtistActivity extends AppCompatActivity {
    BottomNavigationView footer;
    ImageView playPause;
    AnimatedVectorDrawable playToPause;
    AnimatedVectorDrawable pauseToPlay;
    boolean play = PlaybackManager.play;

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

        footer = (BottomNavigationView) findViewById(R.id.footer);
        playPause = (ImageView) footer.findViewById(R.id.play_pause);
        playToPause = (AnimatedVectorDrawable) footer.getContext().getDrawable(R.drawable.avd_play_to_pause);
        pauseToPlay = (AnimatedVectorDrawable) footer.getContext().getDrawable(R.drawable.avd_pause_to_play);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseResumeSong();
            }
        });
        if (PlaybackManager.song != null) {
            playSong(this.getCurrentFocus(), PlaybackManager.song);
        }

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
        Collections.sort(songs, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        mAdapter = new ArtistSongsListAdapter(songs);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void playSong(View v, Song song) {
        PlaybackManager.song=song;

        if (footer.getVisibility() == View.GONE)
            footer.setVisibility(View.VISIBLE);

        TextView title = (TextView) footer.findViewById(R.id.footer_title);
        TextView info = (TextView) footer.findViewById(R.id.footer_info);
        ImageView cover = (ImageView) footer.findViewById(R.id.footer_cover);

        title.setText(song.getTitle());
        info.setText(song.getArtist() + " • " + song.getAlbum());
        if (song.getCover() != null) {
            cover.setImageBitmap(song.getCover());
        } else {
            cover.setImageResource(R.drawable.ic_album);
        }

        AnimatedVectorDrawable drawable = playToPause;
        if (play) {
            playPause.setImageDrawable(drawable);
            drawable.start();
            play = !play;
        }
    }
    public void pauseResumeSong() {
        AnimatedVectorDrawable drawable = play ? playToPause : pauseToPlay;
        playPause.setImageDrawable(drawable);
        drawable.start();
        play = !play;
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
