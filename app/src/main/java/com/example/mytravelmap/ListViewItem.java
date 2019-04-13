package com.example.mytravelmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class ListViewItem
        implements Serializable {
    private String img;
    private String title;
    private String content;
    private double longitude;
    private double latitude;
    private String id;

    public ListViewItem(String img, String title, String content, LatLng latLng, String id) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.longitude = latLng.longitude;
        this.latitude = latLng.latitude;
        this.id = id;
    }

    public ListViewItem(String img, String title, String content, double longitude, double latitude) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public ListViewItem(String img, String title, String content, double longitude, double latitude, String id) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.longitude = longitude;
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
        LatLng latLng = new LatLng(latitude, longitude);
        return latLng;
    }

    public String getId() {
        return id;
    }

    public void setData(String img, String title, String content, LatLng latLng) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.longitude = latLng.longitude;
        this.latitude = latLng.latitude;
    }
}
