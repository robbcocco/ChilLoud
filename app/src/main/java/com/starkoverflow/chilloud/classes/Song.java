package com.starkoverflow.chilloud.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private long id;
    private String title;
    private String album;
    private String artist;
    private int track;

    public Song(long songID, String title, String album, String artist, int track) {
        id=songID;
        this.title=title;
        this.album=album;
        this.artist=artist;
        this.track=track;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        title = in.readString();
        album = in.readString();
        artist = in.readString();
        track = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeInt(track);
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

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getAlbum(){return album;}
    public String getArtist(){return artist;}
}
