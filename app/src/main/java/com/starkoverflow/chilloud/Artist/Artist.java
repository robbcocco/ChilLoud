package com.starkoverflow.chilloud.Artist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.starkoverflow.chilloud.Album.Album;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Artist implements Parcelable {
    private long id;
    private String artist;
    private Bitmap cover;
    private ArrayList<Album> albums;

    public Artist(long songID, String artist) {
        id=songID;
        this.artist=artist;
        this.albums=new ArrayList<Album>();
        String url=getArtistPicture(this.artist);
//        this.cover=getBitmapFromURL("https://i.scdn.co/image/593a2eef7616479f4c6e0e055fb68b18216357a0");
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

    public static String getArtistPicture(String artist) {
        final String[] url = {null};
        Fuel.get("https://api.spotify.com/v1/search?query="+artist+"&type=artist&market=IT&offset=0&limit=20").responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String picUrl = obj.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("url");
                    Log.d("Artist class", "success: " + picUrl);
                    url[0] = picUrl;
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + s + "\"");
                }
            }
            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
            }
        });
        return url[0];
    }

    public static Bitmap getBitmapFromURL(final String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.d("bitmap from url", "success: " + imgUrl);
            return myBitmap;
        } catch (IOException e) {
            Log.d("bitmap from url", "diocan");
            return null;
        }
    }

    public long getID(){return id;}
    public String getArtist(){return artist;}
    public Bitmap getCover(){return cover;}
    public ArrayList<Album> getAlbum(){return albums;}
}
