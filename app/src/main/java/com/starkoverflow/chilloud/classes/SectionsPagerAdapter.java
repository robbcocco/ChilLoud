package com.starkoverflow.chilloud.classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.starkoverflow.chilloud.fragments.AlbumsFragment;
import com.starkoverflow.chilloud.fragments.ArtistsFragment;
import com.starkoverflow.chilloud.fragments.SongsFragment;

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
        switch (position){
            case 0:
                return SongsFragment.newInstance(LibraryFactory.getLibrary());
            case 1:
                return ArtistsFragment.newInstance(LibraryFactory.getLibrary());
            case 2:
                return AlbumsFragment.newInstance(LibraryFactory.getLibrary());
            default:
                return null;
        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a ArtistsFragment (defined as a static inner class below).
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
                return "SONGS";
            case 1:
                return "ARTISTS";
            case 2:
                return "ALBUMS";
        }
        return null;
    }
}
