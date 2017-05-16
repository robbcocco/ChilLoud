package com.starkoverflow.chilloud.Album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.Artist.Artist;
import com.starkoverflow.chilloud.Artist.ArtistsListAdapter;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;
import com.starkoverflow.chilloud.classes.PlaybackManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumActivity extends AppCompatActivity {
    BottomNavigationView footer;
    ImageView playPause;
    AnimatedVectorDrawable playToPause;
    AnimatedVectorDrawable pauseToPlay;
    boolean play = PlaybackManager.play;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Album album = getIntent().getParcelableExtra("album");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitle(album.getAlbum());
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
        ImageView artistPic = (ImageView) findViewById(R.id.album_artist_picture);
        TextView artist = (TextView) findViewById(R.id.album_artist_name);
        TextView year = (TextView) findViewById(R.id.album_year);

        Bitmap albumArt = BitmapFactory.decodeFile(album.getArtPath());
        Palette palette;
        if (album.getArtPath() != null)
            palette = createPaletteSync(albumArt);
        else
            palette = null;
        Palette.Swatch primary = palette.getDarkMutedSwatch();
        Palette.Swatch secondary = palette.getDarkVibrantSwatch();
        if (primary != null) {
            infoBg.setBackgroundColor(primary.getRgb());
            collapsingToolbarLayout.setContentScrimColor(primary.getRgb());
            collapsingToolbarLayout.setBackgroundColor(primary.getRgb());
        } else if (secondary != null) {
            infoBg.setBackgroundColor(secondary.getRgb());
            collapsingToolbarLayout.setContentScrimColor(secondary.getRgb());
            collapsingToolbarLayout.setBackgroundColor(secondary.getRgb());
        }

        artistPic.setImageResource(R.drawable.ic_people);
        FetchArtistInfo fetcher = new FetchArtistInfo();
        fetcher.artistPicture=artistPic;
        fetcher.execute(new Artist(0, album.getArtist()));

        cover.setImageBitmap(albumArt);
        coverbg.setImageBitmap(albumArt);
        artist.setText(album.getArtist());
        year.setText(album.getYear());

        mRecyclerView = (RecyclerView) findViewById(R.id.album_songs_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Song> songs = album.getSongs();
        Collections.sort(songs, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTrack() - b.getTrack();
            }
        });
        mAdapter = new AlbumsSongsListAdapter(songs);
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

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    private class FetchArtistInfo extends AsyncTask<Artist, Integer, Artist> {
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
            }
        }
    }
}
