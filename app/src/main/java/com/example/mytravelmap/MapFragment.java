package com.example.mytravelmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment
        implements OnMapReadyCallback {
    private ArrayList<Marker> markers;

    private Bundle mBundle;
    private View mView;
    private GoogleMap mMap = null;
    private MapView mapView = null;
    public MapInterface mMapInterface;

    public interface MapInterface {
        void moveInfo(String id);

        ArrayList<Marker> addFirst(GoogleMap map);
    }

    public static MapFragment newInstance() {
        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMapInterface = (MapInterface) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(mBundle);
        mBundle = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) mView.findViewById(R.id.map);
        mapView.getMapAsync(this);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null)
            mapView.onStart();
    }

    @Override
    public void onStop() {
        if (mapView != null)
            mapView.onStop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        if (mapView != null)
            mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        if (mapView != null)
            mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        if (mapView != null)
            mapView.onLowMemory();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // 현재 위치 버튼 사용
        mMap.setMyLocationEnabled(true);
        // 마커 클릭 리스너
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMapInterface.moveInfo((String) marker.getTag());
                return false;
            }
        });

        markers = mMapInterface.addFirst(mMap);

        LatLng inha = new LatLng(37.450601, 126.657318);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inha, 7));
    }

    void addMarker(LatLng latLng, String key) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));

        marker.setTag(key);

        markers.add(marker);
    }

    void deleteMarker(String id) {
        int index = 0;
        for (Marker marker : markers) {
            if (marker.getTag().equals(id)) {
                marker.remove();
                markers.remove(index);
                break;
            }
            index++;
        }
    }
}
