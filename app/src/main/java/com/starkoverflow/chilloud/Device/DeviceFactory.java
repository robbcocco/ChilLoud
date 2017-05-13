package com.starkoverflow.chilloud.Device;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DeviceFactory implements Parcelable {
    private static ArrayList<DeviceFactory> devices = new ArrayList<DeviceFactory>();

    private String name;
    private Bitmap picture;

    public DeviceFactory(String name, Bitmap picture) {
        this.name=name;
        this.picture=picture;
    }

    protected DeviceFactory(Parcel in) {
        name = in.readString();
        picture = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<DeviceFactory> CREATOR = new Creator<DeviceFactory>() {
        @Override
        public DeviceFactory createFromParcel(Parcel in) {
            return new DeviceFactory(in);
        }

        @Override
        public DeviceFactory[] newArray(int size) {
            return new DeviceFactory[size];
        }
    };

    public static void createDevice(String name, Bitmap picture) {
        devices.add(new DeviceFactory(name, picture));
    }
    public static void editDevice(String name, Bitmap picture, int position) {
        devices.get(position).name=name;
        devices.get(position).picture=picture;
    }
    public static void deleteDevice(int position) {
        devices.remove(position);
    }

    public static ArrayList<DeviceFactory> getDevices() {
        return devices;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(picture, i);
    }
}
