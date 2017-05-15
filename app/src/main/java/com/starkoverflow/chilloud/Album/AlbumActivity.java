package com.starkoverflow.chilloud.Album;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumActivity extends AppCompatActivity {

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

        ImageView cover = (ImageView) findViewById(R.id.cover);
        ImageView coverbg = (ImageView) findViewById(R.id.cover_bg);
        ImageView artistPic = (ImageView) findViewById(R.id.album_artist_picture);
        TextView artist = (TextView) findViewById(R.id.album_artist_name);
        TextView year = (TextView) findViewById(R.id.album_year);

        cover.setImageBitmap(BitmapFactory.decodeFile(album.getArtPath()));
        coverbg.setImageBitmap(BitmapFactory.decodeFile(album.getArtPath()));
        artist.setText(album.getArtist());

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
}
