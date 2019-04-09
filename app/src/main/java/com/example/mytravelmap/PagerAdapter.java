package com.example.mytravelmap;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_NUMBER = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return ListFragment.newInstance();
        else if (position == 1)
            return MapFragment.newInstance();
        else if (position == 2)
            return MyFragment.newInstance();
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
            return "첫번째 탭";
        else if (position == 1)
            return "두번째 탭";
        else if (position == 2)
            return "세번째 탭";
        else
            return null;
    }
}
