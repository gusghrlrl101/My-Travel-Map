package com.example.mytravelmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.maps.MapView;

public class MainActivity extends AppCompatActivity
        implements MapFragment.MapInterface {
    final private int MY_PERMISSION = 512;
    final private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

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
            mViewPager.setAdapter(mPagerAdapter);

            TabLayout mTab = (TabLayout) findViewById(R.id.tabs);
            mTab.setupWithViewPager(mViewPager);

            FloatingActionButton myFab = findViewById(R.id.fab);
            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addItem();
                }
            });
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

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            ListViewItem item = (ListViewItem) data.getSerializableExtra("item");

            Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
            MyListFragment listFragment = (MyListFragment) page;
            int cnt = listFragment.addData(item.getImg(), item.getTitle(), item.getContent(), item.getLatLng());

            page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1);
            MapFragment mapFragment = (MapFragment) page;

            System.out.println("**************: " + cnt);
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
