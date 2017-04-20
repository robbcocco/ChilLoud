package com.starkoverflow.chilloud.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.classes.LibraryFactory;
import com.starkoverflow.chilloud.classes.Song;
import com.starkoverflow.chilloud.classes.SongsListAdapter;

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//    String songList[] = {"Madre", "Benefit", "Pork Soda", "Pink Guy", "Mainstream", "Legendary Tales", "Queen"};

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
    public static SongsFragment newInstance(ArrayList<LibraryFactory> library) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SONGS, library);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);

        ArrayList<LibraryFactory> library = getArguments().getParcelableArrayList(ARG_SONGS);
        ArrayList<Song> songs = library.get(0).getSongs();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.songs_list);
        // use a linear layout manager
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new SongsListAdapter(songs);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(
//                        getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//                        toolbar.setTitle(dbList[position]);
//                        toggleExpandedMenus();
//                        switch (position) {
//                            case 0:
//                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
//                                break;
//                            case 1:
//                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
//                                break;
//                            case 2:
//                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
//                                break;
//                            case 3:
//                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
//                                break;
//                        }
//                    }
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );

        return rootView;
    }
}
