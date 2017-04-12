package com.starkoverflow.chilloud.classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.starkoverflow.chilloud.fragments.ArtistsFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//            switch (position){
//                case 0:
//        return ArtistsFragment.newInstance(position + 1);
//                case 1:
//        return ArtistsFragment.newInstance(position + 1);
//                case 2:
//        return ArtistsFragment.newInstance(position + 1);
//                default:
//                    return null;
//            }
        // getItem is called to instantiate the fragment for the given page.
        // Return a ArtistsFragment (defined as a static inner class below).
        return ArtistsFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PLAYLISTS";
            case 1:
                return "ARTISTS";
            case 2:
                return "ALBUMS";
        }
        return null;
    }
}
