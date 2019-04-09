package com.example.mytravelmap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class AddItemActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private ImageButton imageButton;
    private Drawable buttonImg;
    private GoogleMap map;
    private Marker marker = null;
    private MyInterface myInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        myInterface = (MyInterface) getIntent().getSerializableExtra("interface");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ImageButton
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageHelper.selectImage(AddItemActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri imageUri = PickImageHelper.getPickImageResultUri(this, data);
            System.out.println(imageUri);
            try {
                InputStream is = this.getContentResolver().openInputStream(imageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                buttonImg = new BitmapDrawable(getResources(), bitmap);

                imageButton.setImageDrawable(buttonImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItem(View view) {
        EditText editTitle = (EditText) findViewById(R.id.editText_title);
        EditText editContent = (EditText) findViewById(R.id.editText_content);

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (buttonImg == null) {
            Toast toast = Toast.makeText(this, "사진을 정해주세요", Toast.LENGTH_SHORT);
            toast.show();
        } else if (TextUtils.isEmpty(title)) {
            Toast toast = Toast.makeText(this, "제목을 적어주세요", Toast.LENGTH_SHORT);
            toast.show();
        } else if (TextUtils.isEmpty(content)) {
            Toast toast = Toast.makeText(this, "내용을 적어주세요", Toast.LENGTH_SHORT);
            toast.show();
        } else if (marker == null) {
            Toast toast = Toast.makeText(this, "위치를 터치해주세요", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            myInterface.addData();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // 현재 위치 버튼 사용
        map.setMyLocationEnabled(true);

        // 지도 클릭 리스너
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker == null)
                    marker = map.addMarker(new MarkerOptions().position(latLng));
                else
                    marker.setPosition(latLng);
            }
        });
    }
}