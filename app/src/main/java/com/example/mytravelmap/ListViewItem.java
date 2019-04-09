package com.example.mytravelmap;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.Marker;

public class ListViewItem {
    private Drawable img;
    private Marker marker;
    private String title;
    private String content;

    // 생성자
    public ListViewItem(Drawable img, Marker marker, String title, String content) {
        this.img = img;
        this.marker = marker;
        this.title = title;
        this.content = content;
    }

    // getter
    public Drawable getImg() {
        return img;
    }

    public Marker getMarker() {
        return marker;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
