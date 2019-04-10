package com.example.mytravelmap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.io.Serializable;

public class ListViewItem
        implements Serializable {
    private String img;
    private String title;
    private String content;
    private double longitude;
    private double latitude;
    private int id;

    // 생성자
    public ListViewItem(String img, String title, String content, LatLng latLng, int id) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.longitude = latLng.longitude;
        this.latitude = latLng.latitude;
        this.id = id;
    }

    public ListViewItem(String img, String title, String content, double longitute, double latitude) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.longitude = longitute;
        this.latitude = latitude;
    }

    public ListViewItem(String img, String title, String content, double longitute, double latitude, int id) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.longitude = longitute;
        this.latitude = latitude;
        this.id = id;
    }

    // getter
    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LatLng getLatLng() {
        LatLng latLng = new LatLng(longitude, latitude);
        return latLng;
    }

    public int getId() {
        return id;
    }
}
