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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
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

            String DB_PATH;
            if (android.os.Build.VERSION.SDK_INT >= 4.2)
                DB_PATH = getApplicationInfo().dataDir + "/databases/";
            else
                DB_PATH = "/data/data/" + getPackageName() + "/databases/";

            File db_dir = new File(DB_PATH);
            if (!db_dir.exists())
                db_dir.mkdirs();

            sqLiteDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "hyunho.db", null);
            sqLiteDB.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "LIST (ID TEXT PRIMARY KEY, IMG TEXT, TITLE TEXT, CONTENT TEXT, LONGITUDE DOUBLE, LATITUDE DOUBLE)");
            System.out.println("&&&&&&&&&&&&&&&&&&");

        }
    }

    private void addItem() {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, mViewPager.getCurrentItem());
    }

    @Override
    public void moveInfo(String id) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("id", id);

        String select = "SELECT * FROM LIST WHERE ID=";
        Cursor cursor = sqLiteDB.rawQuery(select, null);

        startActivity(intent);
    }

    @Override
    public void moveInfo(String id, ListViewAdapter adapter) {
        Intent intent = new Intent(this, InfoActivity.class);
        ListViewItem item = new ListViewItem("", "", "", 0.0, 0.0);
        for (ListViewItem data : adapter.getData()) {
            if (data.getId().equals(id)) {
                item.setData(data.getImg(), data.getTitle(), data.getContent(), data.getLatLng());
                break;
            }
        }

        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void addFirst(GoogleMap map) {
        String select = "SELECT * FROM LIST";
        Cursor cursor = sqLiteDB.rawQuery(select, null);

        while (cursor.moveToNext()) {
            double longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
            double latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
            String id = cursor.getString(cursor.getColumnIndex("ID"));

            LatLng latLng = new LatLng(longitude, latitude);
            Marker marker = map.addMarker(new MarkerOptions().position(latLng));
            marker.setTag(id);
        }
    }

    @Override
    public void addFirst(ListViewAdapter adapter) {
        String select = "SELECT * FROM LIST";
        Cursor cursor = sqLiteDB.rawQuery(select, null);

        while (cursor.moveToNext()) {
            String img = cursor.getString(cursor.getColumnIndex("IMG"));
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            String content = cursor.getString(cursor.getColumnIndex("CONTENT"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
            double latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
            String id = cursor.getString(cursor.getColumnIndex("ID"));

            LatLng latLng = new LatLng(longitude, latitude);
            adapter.addData(img, title, content, latLng, id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String key = sdf.format(date);

            ListViewItem item = (ListViewItem) data.getSerializableExtra("item");

            Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
            MyListFragment listFragment = (MyListFragment) page;

            listFragment.addData(item.getImg(), item.getTitle(), item.getContent(), item.getLatLng(), key);

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
