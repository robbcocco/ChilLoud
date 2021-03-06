package com.starkoverflow.chilloud.Song;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Library.LibraryFactory;

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SONGS = "songs";

    public SongsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SongsFragment newInstance(LibraryFactory library) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SONGS, library);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);

        LibraryFactory library = getArguments().getParcelable(ARG_SONGS);
        ArrayList<Song> songs = library.getSongs();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.songs_list);
        // use a linear layout manager
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new SongsListAdapter(songs);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
