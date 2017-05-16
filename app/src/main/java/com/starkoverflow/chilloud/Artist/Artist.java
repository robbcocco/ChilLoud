package com.starkoverflow.chilloud.Artist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.starkoverflow.chilloud.Album.Album;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Artist implements Parcelable {
    private long id;
    private String artist;
    private ArrayList<Album> albums;
    private String url;
    private Bitmap cover;
    private Palette palette;

    public Artist(long songID, String artist) {
        id=songID;
        this.artist=artist;
        this.albums=new ArrayList<Album>();
        this.url=null;
        this.cover=null;
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        artist = in.readString();
        albums = in.createTypedArrayList(Album.CREATOR);
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(artist);
        dest.writeTypedList(albums);
        dest.writeString(url);
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

    public void getArtistUrl() {
        try {
            URL url = new URL("https://api.spotify.com/v1/search?query="+this.artist+"&type=artist&market=IT&offset=0&limit=20");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);
            }
            String s = buffer.toString();
            JSONObject obj = new JSONObject(s);
            String picUrl = obj.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("url");
            Log.d("bitmap from url", "success: " + this.url);
            this.url=picUrl;
        } catch (Throwable t) {
            Log.d("bitmap from url", "ops " + this.url);
            this.url=null;
        }
    }

    public void getBitmapFromURL() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap cover = BitmapFactory.decodeStream(input);
            Log.d("bitmap from url", "success: " + this.url);
            this.cover = cover;
            this.palette = createPaletteSync(this.cover);
        } catch (IOException e) {
            Log.d("bitmap from url", "ops " + this.url);
            this.cover=null;
        }
    }

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    public long getID(){return id;}
    public String getArtist(){return artist;}
    public ArrayList<Album> getAlbum(){return albums;}
    public String getUrl(){return url;}
    public Bitmap getCover(){return cover;}
    public Palette getPalette(){return palette;}
}
