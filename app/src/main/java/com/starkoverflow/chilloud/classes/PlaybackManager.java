package com.starkoverflow.chilloud.classes;

import android.content.ContentUris;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Song.Song;

import java.io.IOException;

public class PlaybackManager {
    private static BottomNavigationView footer;
    private static Song song;
    private static boolean play=true;

    private static ImageView playPause;
    private static AnimatedVectorDrawable playToPause;
    private static AnimatedVectorDrawable pauseToPlay;

    public static void setFooter(View footer) {
        PlaybackManager.footer = (BottomNavigationView) footer;
        playPause = (ImageView) footer.findViewById(R.id.play_pause);
        playToPause = (AnimatedVectorDrawable) footer.getContext().getDrawable(R.drawable.avd_play_to_pause);
        pauseToPlay = (AnimatedVectorDrawable) footer.getContext().getDrawable(R.drawable.avd_pause_to_play);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseResumeSong();
            }
        });
    }

    public static void playSong(View v, Song song) {
//        PlaybackManager.song=song;
//        long id = song.getID();
//        Uri contentUri = ContentUris.withAppendedId(
//                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
//
//        MediaPlayer mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mMediaPlayer.setDataSource(footer.getContext(), contentUri);
//            mMediaPlayer.start();
//            Log.d("Manager", "Y " + id);
//        } catch (IOException e) {
//            Log.d("Manager", "N");
//            e.printStackTrace();
//        }

        if (footer.getVisibility() == View.GONE)
            footer.setVisibility(View.VISIBLE);

        TextView title = (TextView) footer.findViewById(R.id.footer_title);
        TextView album = (TextView) footer.findViewById(R.id.footer_album);
        TextView artist = (TextView) footer.findViewById(R.id.footer_artist);
        ImageView cover = (ImageView) footer.findViewById(R.id.footer_cover);

        title.setText(song.getTitle());
        album.setText(song.getAlbum());
        artist.setText(song.getArtist());
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

    public static void pauseResumeSong() {
        AnimatedVectorDrawable drawable = play ? playToPause : pauseToPlay;
        playPause.setImageDrawable(drawable);
        drawable.start();
        play = !play;
    }
}
