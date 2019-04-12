package com.example.mytravelmap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class InfoActivity extends FragmentActivity
        implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng latLng;
    private String id;
    private ListViewItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        item = (ListViewItem) getIntent().getSerializableExtra("item");
        System.out.println("create: " + item.getId());

        ImageView imageView = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.textview_info_title);
        TextView content = findViewById(R.id.textview_info_content);

        latLng = item.getLatLng();
        id = item.getId();

        imageView.setImageDrawable(Drawable.createFromPath(item.getImg()));
        title.setText(item.getTitle());
        content.setText(item.getContent());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setTag(id);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    public void share() {

    }

    public void delete(View v) {
        Intent intent = new Intent();
        intent.putExtra("id", item.getId());
        System.out.println(item.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
