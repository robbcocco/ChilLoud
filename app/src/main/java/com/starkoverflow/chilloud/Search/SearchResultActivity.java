package com.starkoverflow.chilloud.Search;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.starkoverflow.chilloud.Album.Album;
import com.starkoverflow.chilloud.Album.AlbumActivity;
import com.starkoverflow.chilloud.Artist.Artist;
import com.starkoverflow.chilloud.Artist.ArtistActivity;
import com.starkoverflow.chilloud.Library.LibraryFactory;
import com.starkoverflow.chilloud.Main.MainActivity;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;
import com.starkoverflow.chilloud.classes.PlaybackManager;
import com.starkoverflow.chilloud.classes.RecyclerItemClickListener;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    BottomNavigationView footer;
    ImageView playPause;
    AnimatedVectorDrawable playToPause;
    AnimatedVectorDrawable pauseToPlay;
    boolean play = PlaybackManager.play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
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

        handleIntent(getIntent());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search, menu);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }

    private void showResults(String query) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search: " + query);
        ArrayList<LibraryFactory> libraries = LibraryFactory.getLibrary();

        RecyclerView artistsRecyclerView;
        RecyclerView.Adapter artistsAdapter;
        RecyclerView.LayoutManager artistsLayoutManager;
        final ArrayList<Artist> artists = new ArrayList<Artist>();
        for (int i = 0; i < libraries.size(); i++) {
            for (int j = 0; j < libraries.get(i).getArtists().size(); j++) {
                if (libraries.get(i).getArtists().get(j).getArtist().toLowerCase().contains(query)) {
                    artists.add(libraries.get(i).getArtists().get(j));
                }
            }
        }
        artistsRecyclerView = (RecyclerView) findViewById(R.id.search_list_artists);
        artistsLayoutManager = new LinearLayoutManager(this);
        artistsRecyclerView.setLayoutManager(artistsLayoutManager);
        artistsAdapter = new SearchArtistsListAdapter(artists);
        artistsRecyclerView.setAdapter(artistsAdapter);
        artistsRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, artistsRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(SearchResultActivity.this, ArtistActivity.class);
                        intent.putExtra("artist", artists.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        RecyclerView albumsRecyclerView;
        RecyclerView.Adapter albumsAdapter;
        RecyclerView.LayoutManager albumsLayoutManager;
        final ArrayList<Album> albums = new ArrayList<Album>();
        for (int i = 0; i < libraries.size(); i++) {
            for (int j = 0; j < libraries.get(i).getAlbums().size(); j++) {
                if (libraries.get(i).getAlbums().get(j).getAlbum().toLowerCase().contains(query)) {
                    albums.add(libraries.get(i).getAlbums().get(j));
                }
            }
        }
        albumsRecyclerView = (RecyclerView) findViewById(R.id.search_list_albums);
        albumsLayoutManager = new LinearLayoutManager(this);
        albumsRecyclerView.setLayoutManager(albumsLayoutManager);
        albumsAdapter = new SearchAlbumsListAdapter(albums);
        albumsRecyclerView.setAdapter(albumsAdapter);
        albumsRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, albumsRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(SearchResultActivity.this, AlbumActivity.class);
                        intent.putExtra("album", albums.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        RecyclerView songsRecyclerView;
        RecyclerView.Adapter songsAdapter;
        RecyclerView.LayoutManager songsLayoutManager;
        final ArrayList<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < libraries.size(); i++) {
            for (int j = 0; j < libraries.get(i).getSongs().size(); j++) {
                if (libraries.get(i).getSongs().get(j).getTitle().toLowerCase().contains(query)) {
                    songs.add(libraries.get(i).getSongs().get(j));
                }
            }
        }
        songsRecyclerView = (RecyclerView) findViewById(R.id.search_list_songs);
        songsLayoutManager = new LinearLayoutManager(this);
        songsRecyclerView.setLayoutManager(songsLayoutManager);
        songsAdapter = new SearchSongsListAdapter(songs);
        songsRecyclerView.setAdapter(songsAdapter);
        songsRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, songsRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        PlaybackManager.playSong(view, songs.get(position));
                        finish();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
}