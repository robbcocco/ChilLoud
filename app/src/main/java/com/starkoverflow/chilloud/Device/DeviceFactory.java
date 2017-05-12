package com.starkoverflow.chilloud.Device;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DeviceFactory {
    private static ArrayList<DeviceFactory> devices = new ArrayList<DeviceFactory>();

    private String name;
    private Bitmap picture;

    public DeviceFactory(String name, Bitmap picture) {
        this.name=name;
        this.picture=picture;
    }

    public static void createDevice(String name, Bitmap picture) {
        devices.add(new DeviceFactory(name, picture));
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
}
