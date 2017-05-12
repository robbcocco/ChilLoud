package com.starkoverflow.chilloud.Library;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import com.starkoverflow.chilloud.Album.Album;
import com.starkoverflow.chilloud.Artist.Artist;
import com.starkoverflow.chilloud.Song.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LibraryFactory implements Parcelable{
    private static ArrayList<LibraryFactory> library = new ArrayList<LibraryFactory>();
    private static final String TAG = "Media Factory";

    private String name;
    private ArrayList<Artist> artists;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;

    public LibraryFactory(String name, ArrayList<Artist> artists, ArrayList<Album> albums, ArrayList<Song> songs) {
        this.name=name;
        this.artists=artists;
        this.albums=albums;
        this.songs=songs;
    }

    protected LibraryFactory(Parcel in) {
        artists = in.createTypedArrayList(Artist.CREATOR);
        albums = in.createTypedArrayList(Album.CREATOR);
        songs = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<LibraryFactory> CREATOR = new Creator<LibraryFactory>() {
        @Override
        public LibraryFactory createFromParcel(Parcel in) {
            return new LibraryFactory(in);
        }

        @Override
        public LibraryFactory[] newArray(int size) {
            return new LibraryFactory[size];
        }
    };

    public static void makeMediaLibrary(String name, ContentResolver musicResolver, Context context) {
        Uri musicUri = android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor libraryCursor = musicResolver.query(musicUri, null, null, null, null);

        ArrayList<Artist> artistsList = new ArrayList<Artist>();
        ArrayList<Album> albumsList = new ArrayList<Album>();
        ArrayList<Song> songsList = new ArrayList<Song>();

        Log.d(TAG, "makeMediaLibrary: library cursor");
        if(libraryCursor!=null && libraryCursor.moveToFirst()){
            //get columns
            int artistIdColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
            int artistColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            //add to list
            do {
                long artistId = libraryCursor.getLong(artistIdColumn);
                String artist = libraryCursor.getString(artistColumn);
                if (!Artist.contains(artistsList, artist))
                    artistsList.add(new Artist(artistId, artist));
            }
            while (libraryCursor.moveToNext());
        }

        musicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        libraryCursor = musicResolver.query(musicUri, null, null, null, null);
        if(libraryCursor!=null && libraryCursor.moveToFirst()){
            //get columns
            int albumIdColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Albums._ID);
            int albumColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int albumArtistColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ARTIST);
            int dateColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Albums.FIRST_YEAR);
            int artColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.AlbumColumns.ALBUM_ART);
            //add to list
            do {
                long albumId = libraryCursor.getLong(albumIdColumn);
                String album = libraryCursor.getString(albumColumn);
                String albumArtist = libraryCursor.getString(albumArtistColumn);
                String date = libraryCursor.getString(dateColumn);
                String artPath = null;
                if (artColumn >= 0)
                    artPath = libraryCursor.getString(artColumn);
                Log.d(TAG, "makeMediaLibrary: artPath" + artPath);
                if (!Album.contains(albumsList, album, albumArtist)) {
                    Album a = new Album(albumId, album, albumArtist, artPath);
                    albumsList.add(a);
                    for (Artist item : artistsList) {
                        if (item.getArtist().equals(albumArtist)) {
                            item.addAlbum(a);
                        }
                    }
                }
            }
            while (libraryCursor.moveToNext());
        }

        musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        libraryCursor = musicResolver.query(musicUri, null, null, null, null);
        if(libraryCursor!=null && libraryCursor.moveToFirst()){
            //get columns
            int isMusicColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_MUSIC);

            int idColumn = libraryCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int titleColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int trackAlbumColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int trackArtistColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int trackColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Media.TRACK);
            int durationColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            //add to list
            do {
                if (libraryCursor.getLong(isMusicColumn) != 0) {
                    long id = libraryCursor.getLong(idColumn);
                    String title = libraryCursor.getString(titleColumn);
                    String trackAlbum = libraryCursor.getString(trackAlbumColumn);
                    String trackArtist = libraryCursor.getString(trackArtistColumn);
                    int track = libraryCursor.getInt(trackColumn);
                    int duration = libraryCursor.getInt(durationColumn);
                    Song s = new Song(id, title, trackAlbum, trackArtist, track, duration);
                    if (!Song.contains(songsList, title, trackAlbum, trackArtist)) {
                        songsList.add(s);
                        for (Album item : albumsList) {
                            if (item.getAlbum().equals(trackAlbum) && item.getArtist().equals(trackArtist)) {
                                item.addSong(s);
                                if (item.getCover() != null)
                                    s.addCover(item.getCover());
                            }
                        }
                    }
                }
            }
            while (libraryCursor.moveToNext());
        }

        Log.d(TAG, "makeMediaLibrary: sorting library");
        Collections.sort(artistsList, new Comparator<Artist>(){
            public int compare(Artist a, Artist b){
                return a.getArtist().compareTo(b.getArtist());
            }
        });
        Collections.sort(albumsList, new Comparator<Album>(){
            public int compare(Album a, Album b){
                return a.getAlbum().compareTo(b.getAlbum());
            }
        });
        Collections.sort(songsList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        library.add(new LibraryFactory(name, artistsList, albumsList, songsList));
        Log.d(TAG, "makeMediaLibrary: library added");
    }

    public static ArrayList<LibraryFactory> getLibrary() {
        return library;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(artists);
        parcel.writeTypedList(albums);
        parcel.writeTypedList(songs);
    }
}
