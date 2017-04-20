package com.starkoverflow.chilloud.classes;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LibraryFactory implements Parcelable{
    private static final String TAG = "Library Factory";
    private ArrayList<Artist> artists;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;

    private static ArrayList<LibraryFactory> library = new ArrayList<LibraryFactory>();

    public LibraryFactory(ArrayList<Artist> artists, ArrayList<Album> albums, ArrayList<Song> songs) {
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

    public static void makeSongList(ContentResolver musicResolver) {
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor artistCursor = musicResolver.query(musicUri, null, null, null, null);
        Cursor albumCursor = musicResolver.query(musicUri, null, null, null, null);
        Cursor songCursor = musicResolver.query(musicUri, null, null, null, null);
        ArrayList<Artist> artistsList = new ArrayList<Artist>();
        ArrayList<Album> albumsList = new ArrayList<Album>();
        ArrayList<Song> songsList = new ArrayList<Song>();

        Log.d(TAG, "makeSongList: artist cursor");
        if(artistCursor!=null && artistCursor.moveToFirst()){
            //get columns
            int idColumn = artistCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
            int artistColumn = artistCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            //add artists to list
            do {
                long id = artistCursor.getLong(idColumn);
                String artist = artistCursor.getString(artistColumn);
                if (!Artist.contains(artistsList, artist))
                    artistsList.add(new Artist(id, artist));
            }
            while (artistCursor.moveToNext());
        }
        Log.d(TAG, "makeSongList: album cursor");
        if(albumCursor!=null && albumCursor.moveToFirst()){
            //get columns
            int idColumn = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums._ID);
            int albumColumn = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int artistColumn = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ARTIST);
            int dateColumn = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.FIRST_YEAR);
            int artColumn = albumCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM_ART);
            //add albums to list
            do {
                long id = albumCursor.getLong(idColumn);
                String album = albumCursor.getString(albumColumn);
                String artist = albumCursor.getString(artistColumn);
//                String date = albumCursor.getString(dateColumn);
                String artPath = null;
                if (artColumn >= 0)
                    artPath = albumCursor.getString(artColumn);
                if (!Album.contains(albumsList, album, artist)) {
                    Album a = new Album(id, album, artist, artPath);
                    albumsList.add(a);
//                    artistsList.get(artistsList.indexOf(artist)).addAlbum(a);
                }
            }
            while (albumCursor.moveToNext());
        }
        Log.d(TAG, "makeSongList: song cursor");
        if(songCursor!=null && songCursor.moveToFirst()){
            //get columns
            int idColumn = songCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int titleColumn = songCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int albumColumn = songCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int artistColumn = songCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int trackColumn = songCursor.getColumnIndex
                    (MediaStore.Audio.Media.TRACK);
            //add songs to list
            do {
                long id = songCursor.getLong(idColumn);
                String title = songCursor.getString(titleColumn);
                String album = songCursor.getString(albumColumn);
                String artist = songCursor.getString(artistColumn);
                int track = songCursor.getInt(trackColumn);
                Song s = new Song(id, title, album, artist, track);
                songsList.add(s);
//                albumsList.get(Album.getAlbum(albumsList, album, artist)).addSong(s);
            }
            while (songCursor.moveToNext());
        }

        Log.d(TAG, "makeSongList: adding library");
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
        library.add(new LibraryFactory(artistsList, albumsList, songsList));
        Log.d(TAG, "makeSongList: library added");
    }

    public static ArrayList<LibraryFactory> getLibrary() {
        return library;
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
