package com.example.mytravelmap;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_NUMBER = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return MapFragment.newInstance();
        else if (position == 1)
            return MyListFragment.newInstance();
        else if (position == 2)
            return CalendarFragment.newInstance();
        else
            return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "지도";
        else if (position == 1)
            return "목록";
        else if (position == 2)
            return "달력";
        else
            return null;
    }
}
