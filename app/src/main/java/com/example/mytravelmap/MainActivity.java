package com.example.mytravelmap;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements MapFragment.MapInterface, MyInterface{
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private MyInterface myInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout mTab = (TabLayout) findViewById(R.id.tabs);
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    public void moveInfo(String id) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("inteface", myInterface);
        startActivity(intent);
    }

    @Override
    public void addData() {
        System.out.println("@@@@@@@@@@@@@");
    }
}
