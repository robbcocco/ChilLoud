package com.starkoverflow.chilloud;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.starkoverflow.chilloud.Artists;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    // An array of tab titles(labels)
    String titles[];
    // Should be initialized with number of tabs
    int numOfTabs;
    // Use support fragment for your app to work on earlier devices
    // fragments were introduced in android 3.0, api 11
    Fragment fragment;
    // Constructor, initializes passed arguments with class values
    public ViewPagerAdapter(FragmentManager fm, String titles[], int numOfTabs){
        super(fm);
        this.titles = titles;
        this.numOfTabs = numOfTabs;
    }
    // This method returns a fragment object, of the selected tab
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                //fragment = new Artists();
                fragment = new Fragment();
                return fragment;
            case 1:
                fragment = new Fragment();
                return fragment;
            default:
                return null;
        }
    }
    // Returns the tab's title
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
    // Returns the number of tabs
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
