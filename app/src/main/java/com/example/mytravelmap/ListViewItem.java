package com.example.mytravelmap;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class ListViewItem {
    private Drawable img;
    private String title;
    private String content;
    private LatLng latLng;

    // 생성자
    public ListViewItem(Drawable img, String title, String content, LatLng latLng) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.latLng = latLng;
    }

    // getter
    public Drawable getImg() {
        return img;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
