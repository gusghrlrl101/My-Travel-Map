package com.example.mytravelmap;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements MapFragment.MapInterface {
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void addItem() {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, mViewPager.getCurrentItem());
    }

    @Override
    public void moveInfo(String id) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("id", id);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("!!!!" + resultCode);
        if (resultCode == RESULT_OK) {
            mViewPager.setCurrentItem(requestCode, true);
            ListViewItem item = (ListViewItem) data.getSerializableExtra("item");
            System.out.println("@@@@" + item.getTitle());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
