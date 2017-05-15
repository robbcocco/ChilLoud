package com.starkoverflow.chilloud.Album;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;

import com.starkoverflow.chilloud.Song.Song;

import java.util.ArrayList;

public class Album implements Parcelable {
    private long id;
    private String album;
    private String artist;
    private String artPath;
    private Bitmap cover;
    private Palette palette;
    private ArrayList<Song> songs;

    public Album(long songID, String album, String artist, String artPath) {
        id=songID;
        this.album=album;
        this.artist=artist;
        this.artPath=artPath;
        this.cover=BitmapFactory.decodeFile(artPath);
        if (artPath != null)
            this.palette = createPaletteSync(this.cover);
        else
            this.palette = null;
        this.songs=new ArrayList<Song>();
    }

    protected Album(Parcel in) {
        id = in.readLong();
        album = in.readString();
        artist = in.readString();
        artPath = in.readString();
        songs = in.createTypedArrayList(Song.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(artPath);
        dest.writeTypedList(songs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public static boolean contains(ArrayList<Album> list, String album, String artist) {
        for (Album item : list) {
            if (item.getAlbum().equals(album) && item.getArtist().equals(artist)) {
                return true;
            }
        }
        return false;
    }

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    public long getID(){return id;}
    public String getAlbum(){return album;}
    public String getArtist(){return artist;}
    public String getArtPath(){return artPath;}
    public Bitmap getCover(){return cover;}
    public Palette getPalette(){return palette;}
    public ArrayList<Song> getSongs(){return songs;}
}
