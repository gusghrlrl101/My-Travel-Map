package com.example.mytravelmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment
        implements OnMapReadyCallback {
    final private int MY_PERMISSION_ACCESS_LOCATION = 511;
    final private String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    static private boolean isGranted = false;
    private Bundle mBundle;
    private View mView;
    private GoogleMap mMap = null;
    private MapView mapView = null;
    public MapInterface mMapInterface;

    private double latitude, longitude;

    public static MapFragment newInstance() {
        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public interface MapInterface {
        void moveInfo(String id);
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

        // 권한 확인
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(permissions, MY_PERMISSION_ACCESS_LOCATION);
        else
            isGranted = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        if (isGranted) {
            mapView = (MapView) mView.findViewById(R.id.map);
            mapView.getMapAsync(this);
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isGranted && mapView != null)
            mapView.onStart();
    }

    @Override
    public void onStop() {
        if (isGranted && mapView != null)
            mapView.onStop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isGranted && mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGranted && mapView != null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        if (isGranted && mapView != null)
            mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        if (isGranted && mapView != null)
            mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        if (isGranted && mapView != null)
            mapView.onLowMemory();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isGranted && mapView != null)
            mapView.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // 현재 위치 버튼 사용
        mMap.setMyLocationEnabled(true);
        // 지도 클릭 리스너
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }
        });
        // 마커 클릭 리스너
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMapInterface.moveInfo(marker.getId());

                return false;
            }
        });

        // Add a marker in Inha univ. and move the camera
        LatLng inha = new LatLng(37.450601, 126.657318);
        mMap.addMarker(new MarkerOptions()
                .position(inha)
                .title("inha")
                .snippet("Marker in Inha Univ."));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(inha));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                // toast 띄워주기
                Toast toast = Toast.makeText(getContext(), "위치서비스 허용이 필요합니다. 어플을 종료합니다.", Toast.LENGTH_LONG);
                toast.show();

                allGranted = false;
                getActivity().finishAffinity();
            }
        }
        // 허락한 경우 MapView 생성
        if (allGranted) {
            mapView = (MapView) mView.findViewById(R.id.map);
            mapView.getMapAsync(this);
            mapView.onCreate(mBundle);
            mapView.onStart();
            isGranted = true;
        }
    }

    public void myCamera(double latitude, double longitude) {
        // Main Activity에서 리스트뷰 아이템 클릭 시 해당 위치로 이동하는 함수이다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
    }
}
