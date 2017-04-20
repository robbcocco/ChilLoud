package com.starkoverflow.chilloud.classes;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import com.starkoverflow.chilloud.MainActivity;

import java.io.FileDescriptor;
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

    public static void makeSongList(ContentResolver musicResolver, Context context) {
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor libraryCursor = musicResolver.query(musicUri, null, null, null, null);

        ArrayList<Artist> artistsList = new ArrayList<Artist>();
        ArrayList<Album> albumsList = new ArrayList<Album>();
        ArrayList<Song> songsList = new ArrayList<Song>();

        Log.d(TAG, "makeSongList: library cursor");
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

            int artistIdColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
            int artistColumn = libraryCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            //add to list
            do {
                if (libraryCursor.getLong(isMusicColumn) != 0) {
                    long artistId = libraryCursor.getLong(artistIdColumn);
                    String artist = libraryCursor.getString(artistColumn);
                    if (!Artist.contains(artistsList, artist))
                        artistsList.add(new Artist(artistId, artist));

                    long albumId = libraryCursor.getLong(albumIdColumn);
                    String album = libraryCursor.getString(albumColumn);
                    String albumArtist = libraryCursor.getString(albumArtistColumn);
//                    String date = libraryCursor.getString(dateColumn);
                    Cursor cursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {String.valueOf(albumId)},
                            null);
                    String artPath = null;
                    if (cursor.moveToFirst()) {
                        artPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        // do whatever you need to do
                    }
//                    String artPath = null;
//                    if (artColumn >= 0)
//                        artPath = libraryCursor.getString(artColumn);
                    Log.d(TAG, "makeSongList: artPath" + artPath);
                    if (!Album.contains(albumsList, album, albumArtist)) {
                        Album a = new Album(albumId, album, albumArtist, artPath);
                        albumsList.add(a);
//                        artistsList.get(artistsList.indexOf(artist)).addAlbum(a);
                    }

                    long id = libraryCursor.getLong(idColumn);
                    String title = libraryCursor.getString(titleColumn);
                    String trackAlbum = libraryCursor.getString(trackAlbumColumn);
                    String trackArtist = libraryCursor.getString(trackArtistColumn);
                    int track = libraryCursor.getInt(trackColumn);
                    Song s = new Song(id, title, trackAlbum, trackArtist, track);
                    if (!Song.contains(songsList, title, trackAlbum, trackArtist)) {
                        songsList.add(s);
                        for (Album item : albumsList) {
                            if (item.getAlbum().equals(trackAlbum)) {
                                item.addSong(s);
                            }
                        }
                    }
                }
            }
            while (libraryCursor.moveToNext());
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
