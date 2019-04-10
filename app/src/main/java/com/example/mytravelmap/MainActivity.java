package com.example.mytravelmap;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements MapFragment.MapInterface, MyListFragment.ListInterface {
    final private int MY_PERMISSION = 512;
    final private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private SQLiteDatabase sqLiteDB;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private boolean isGranted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                requestPermissions(permissions, MY_PERMISSION);
                break;
            }
        }

        if (isGranted) {
            mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.viewpager);
            mViewPager.setOffscreenPageLimit(3);
            mViewPager.setAdapter(mPagerAdapter);

            TabLayout mTab = (TabLayout) findViewById(R.id.tabs);
            mTab.setupWithViewPager(mViewPager);

            FloatingActionButton myFab = findViewById(R.id.fab);
            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addItem();
                }
            });


            sqLiteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.mytravelmap/databases/hyunho.db", null);
            sqLiteDB.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "LIST (ID TEXT PRIMARY KEY, IMG TEXT, TITLE TEXT, CONTENT TEXT, LONGITUDE DOUBLE, LATITUDE DOUBLE)");
            System.out.println("&&&&&&&&&&&&&&&&&&");

            String select = "SELECT * FROM LIST";
            Cursor cursor = sqLiteDB.rawQuery(select, null);
            System.out.println(cursor.getCount());
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String img = cursor.getString(cursor.getColumnIndex("IMG"));
                    String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                    String content = cursor.getString(cursor.getColumnIndex("CONTENT"));
                    double longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
                    double latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));

                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
                    MyListFragment listFragment = (MyListFragment) page;

                    LatLng latLng = new LatLng(longitude, latitude);
                    listFragment.addData(img, title, content, latLng);
                }
            }
        }
    }

    private void addItem() {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, mViewPager.getCurrentItem());
    }


    @Override
    public void moveInfo(String id) {
//        디비에서 id 찾기
//        ListViewItem item = new ListViewItem(file.toString(), title, content, marker.getPosition().longitude, marker.getPosition().latitude);

        Intent intent = new Intent();
//        intent.putExtra("item", item);

        startActivity(intent);
    }

    @Override
    public void moveInfo(int id) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("id", Integer.toString(id));

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            ListViewItem item = (ListViewItem) data.getSerializableExtra("item");

            Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
            MyListFragment listFragment = (MyListFragment) page;
            listFragment.addData(item.getImg(), item.getTitle(), item.getContent(), item.getLatLng());


            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String key = sdf.format(date);

            ContentValues cv = new ContentValues();
            cv.put("ID", key);
            cv.put("IMG", item.getImg());
            cv.put("TITLE", item.getTitle());
            cv.put("CONTENT", item.getContent());
            cv.put("LONGITUDE", item.getLatLng().longitude);
            cv.put("LATITUDE", item.getLatLng().latitude);
            sqLiteDB.insert("LIST", null, cv);


            page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1);
            MapFragment mapFragment = (MapFragment) page;
            mapFragment.addMarker(item.getLatLng(), key);
        } else {
            // toast 띄워주기
            Toast toast = Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                // toast 띄워주기
                Toast toast = Toast.makeText(this, "권한 허락은 필수입니다. 앱을 종료합니다.", Toast.LENGTH_LONG);
                toast.show();

                allGranted = false;
                finish();
            }
        }

        // 허락한 경우 액티비티 재생성
        if (allGranted) {
            isGranted = true;
            recreate();
        }
    }
}
