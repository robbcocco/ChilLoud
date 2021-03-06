package com.starkoverflow.chilloud.Song;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Song implements Parcelable {
    private long id;
    private String title;
    private String album;
    private String artist;
    private int track;
    private String duration;
    private String artPath;
    private Bitmap cover;
    private Palette palette;

    public Song(long songID, String title, String album, String artist, int track, int duration) {
        id=songID;
        this.title=title;
        this.album=album;
        this.artist=artist;
        this.track=track;
        long minutes=TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds=TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        this.duration=String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);
        this.artPath=null;
        this.cover=null;
        this.palette=null;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        title = in.readString();
        album = in.readString();
        artist = in.readString();
        track = in.readInt();
        duration = in.readString();
        artPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeInt(track);
        dest.writeString(duration);
        dest.writeString(artPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public static boolean contains(ArrayList<Song> list, String song, String album, String artist) {
        for (Song item : list) {
            if (item.getTitle().equals(song) && item.getAlbum().equals(album) && item.getArtist().equals(artist)) {
                return true;
            }
        }
        return false;
    }

    public void addCover(String artPath, Bitmap cover) {
        this.artPath=artPath;
        this.cover=cover;
        this.palette=createPaletteSync(cover);
    }

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getAlbum(){return album;}
    public String getArtist(){return artist;}
    public int getTrack(){return track;}
    public String getDuration(){return duration;}
    public String getArtPath() {return artPath;}
    public Bitmap getCover(){return cover;}
    public Palette getPalette(){return palette;}
}
