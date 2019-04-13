package com.example.mytravelmap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private ImageButton imageButton;
    private Drawable buttonImg;
    private GoogleMap map;
    private Marker marker = null;
    private boolean past;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Map 프래그먼트 연결
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

        // 달력에서 과거를 선택한 경우
        past = getIntent().getBooleanExtra("past", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 이미지를 고른 경우
        if (resultCode == RESULT_OK) {
            Uri imageUri = PickImageHelper.getPickImageResultUri(this, data);
            try {
                // 이미지 버튼에 저장
                InputStream is = this.getContentResolver().openInputStream(imageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                buttonImg = new BitmapDrawable(getResources(), bitmap);
                imageButton.setImageDrawable(buttonImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItem(View view) {
        // 뷰로부터 가져오기
        EditText editTitle = findViewById(R.id.editText_title);
        EditText editContent = findViewById(R.id.editText_content);
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        // 미선택 예외처리
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
        } else
            addData(buttonImg, title, content, marker);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // 현재 위치 버튼 사용
        map.setMyLocationEnabled(true);

        // 기본 시점 이동
        LatLng korea = new LatLng(37.450601, 126.657318);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 6));

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

    private void addData(Drawable buttonImg, String title, String content, Marker marker) {
        // 현재 시각 받아오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 파일명 저장
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String path = sdf.format(date) + ".jpg";
        // 경로 없으면 생성
        Bitmap bitmap = ((BitmapDrawable) buttonImg).getBitmap();
        File dir = new File(Environment.getExternalStorageDirectory() + "/image");
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, path);

        try {
            // 이미지 파일로 저장
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Toast 띄우기
        Toast toast = Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT);
        toast.show();

        // Intent에 Data 저장
        Intent intent = new Intent();
        ListViewItem item = new ListViewItem(file.toString(), title, content, marker.getPosition().longitude, marker.getPosition().latitude);
        intent.putExtra("item", item);
        intent.putExtra("past", past);
        setResult(RESULT_OK, intent);
        finish();
    }
}