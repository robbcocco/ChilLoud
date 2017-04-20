package com.starkoverflow.chilloud.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Artist implements Parcelable {
    private long id;
    private String artist;
    private ArrayList<Album> albums;

    public Artist(long songID, String artist) {
        id=songID;
        this.artist=artist;
        this.albums=null;
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        artist = in.readString();
        albums = in.createTypedArrayList(Album.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(artist);
        dest.writeTypedList(albums);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public static boolean contains(ArrayList<Artist> list, String artist) {
        for (Artist item : list) {
            if (item.getArtist().equals(artist)) {
                return true;
            }
        }
        return false;
    }

    public long getID(){return id;}
    public String getArtist(){return artist;}
    public ArrayList<Album> getAlbum(){return albums;}
}
