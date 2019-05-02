package com.dawnsoft.dawn.audiotask.model;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dawnsoft.dawn.audiotask.frags.AboutFragment;
import com.dawnsoft.dawn.audiotask.frags.HomeFragment;
import com.dawnsoft.dawn.audiotask.frags.SettingsFragment;
import com.dawnsoft.dawn.audiotask.frags.ShareFragment;

/**
 * Created by dawn on 3/17/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 4;
    private String[] tabTitles = new String[]{"Home", "Settings", "Share", "About"};
    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new HomeFragment();
        } else if (position == 1){
            return new SettingsFragment();
        } else if (position == 2){
            return new ShareFragment();
        } else {
            return new AboutFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
