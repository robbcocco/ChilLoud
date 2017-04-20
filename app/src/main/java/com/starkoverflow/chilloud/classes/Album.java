package com.starkoverflow.chilloud.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;

public class Album implements Parcelable {
    private long id;
    private String album;
    private String artist;
    private Drawable art;
    private Palette.Swatch muted;
    private ArrayList<Song> songs;

    public Album(long songID, String album, String artist, String artPath) {
        id=songID;
        this.album=album;
        this.artist=artist;
        if (artPath != null) {
            this.art=Drawable.createFromPath(artPath);
            Palette p = createPaletteSync(BitmapFactory.decodeFile(artPath));
            this.muted = p.getMutedSwatch();
        }
        this.songs=null;
    }

    protected Album(Parcel in) {
        id = in.readLong();
        album = in.readString();
        artist = in.readString();
        songs = in.createTypedArrayList(Song.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(album);
        dest.writeString(artist);
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
        if (this.songs == null)
            this.songs = new ArrayList<Song>();
        this.songs.add(song);
    }

    public static int getAlbum(ArrayList<Album> list, String album, String artist) {
        for (int i=0; i<list.size(); i++) {
            if (list.get(i).getAlbum().equals(album) && list.get(i).getArtist().equals(artist));
                return i;
        }
        return -1;
    }

    public static boolean contains(ArrayList<Album> list, String album, String artist) {
        for (Album item : list) {
            if (item.getAlbum().equals(album) && item.getArtist().equals(artist)) {
                return true;
            }
        }
        return false;
    }

    public long getID(){return id;}
    public String getAlbum(){return album;}
    public String getArtist(){return artist;}
    public Drawable getArt(){return art;}
    public Palette.Swatch getMuted(){return muted;}
    public ArrayList<Song> getSongs(){return songs;}

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }
}
